package com.ark.norns.service;

import com.ark.norns.dao.DeviceDAO;
import com.ark.norns.entity.Device;
import com.ark.norns.entity.entityValidator.DeviceValidator;
import com.ark.norns.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    @Autowired
    protected DeviceValidator deviceValidator;
    @Autowired
    protected DeviceDAO deviceDAO;

    @Autowired
    protected SensorService sensorService;

    public Device eagerFetchDevice(Device device) {
        if (device != null) {
            device.setSensors(sensorService.getSensorDAO().getSensorRepository().findAllByDevice(device));
            return device;
        } else {
            return null;
        }
    }

    public Device persistDevice(Device device) {
        return deviceDAO.persistDevice(device);
    }

    public DeviceRepository getDeviceRepository() {
        return deviceDAO.getDeviceRepository();
    }

    public DeviceValidator getDeviceValidator() {
        return this.deviceValidator;
    }

    public DeviceDAO getDeviceDAO() {
        return this.deviceDAO;
    }

}
