package com.ark.norns.dao;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class DeviceDAO extends _DAO<Device, Long>{
    @Autowired
    protected DeviceDAO(DeviceRepository deviceRepository) {
        super(deviceRepository);
    }

    public DeviceRepository getDeviceRepository() {
        return (DeviceRepository) super.getBaseRepository();
    }

    @Autowired
    private SensorDAO sensorDAO;

    public Device persistDevice(Device device) {
        List<Sensor> sensorList = new ArrayList<>(device.getSensors());
        device = this.save(device);
        if (sensorList != null) {
            for (Sensor sensor : sensorList) {
                sensor.setDevice(device);
            }
            sensorDAO.saveAll(sensorList);
        }
        return device;
    }
}
