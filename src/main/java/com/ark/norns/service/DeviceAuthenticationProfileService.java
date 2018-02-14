package com.ark.norns.service;

import com.ark.norns.dao.DeviceAuthenticationProfileDAO;
import com.ark.norns.entity.DeviceAuthenticationProfile;
import com.ark.norns.entity.entityValidator.DeviceAuthenticationProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceAuthenticationProfileService {
    @Autowired
    private DeviceAuthenticationProfileValidator deviceAuthenticationProfileValidator;
    @Autowired
    private DeviceAuthenticationProfileDAO deviceAuthenticationProfileDAO;

    public void removeEntity(Long id) {
        getDeviceAuthenticationProfileDAO().delete(getDeviceAuthenticationProfileDAO().findOne(id));
    }

    public DeviceAuthenticationProfile persistEntity(DeviceAuthenticationProfile deviceAuthenticationProfile) {
        return getDeviceAuthenticationProfileDAO().save(deviceAuthenticationProfile);
    }

    public DeviceAuthenticationProfileValidator getDeviceAuthenticationProfileValidator() {
        return deviceAuthenticationProfileValidator;
    }

    public DeviceAuthenticationProfileDAO getDeviceAuthenticationProfileDAO() {
        return deviceAuthenticationProfileDAO;
    }
}
