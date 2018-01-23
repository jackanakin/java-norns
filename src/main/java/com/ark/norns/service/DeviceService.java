package com.ark.norns.service;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.entity.entityValidator.DeviceValidator;
import com.ark.norns.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class DeviceService extends _Service<Device, Long> {
    @Autowired
    protected DeviceValidator deviceValidator;

    @Autowired
    protected SensorService sensorService;

    @Autowired
    protected DeviceService(DeviceRepository deviceRepository) {
        super(deviceRepository);
    }

    public Device eagerFetchDevice(Device device) {
        if (device != null) {
            device.setSensors(sensorService.getSensorRepository().findAllByDevice(device));
            return device;
        } else {
            return null;
        }
    }

    public Device persistDevice(Device device) {
        List<Sensor> sensorList = new ArrayList<>(device.getSensors());
        device = getDeviceRepository().save(device);
        if (sensorList != null) {
            for (Sensor sensor : sensorList) {
                sensor.setDevice(device);
            }
            sensorService.getSensorRepository().save(sensorList);
        }
        return device;
    }

    public DeviceRepository getDeviceRepository() {
        return (DeviceRepository) super.getBaseRepository();
    }

    public DeviceValidator getDeviceValidator() {
        return this.deviceValidator;
    }
}
