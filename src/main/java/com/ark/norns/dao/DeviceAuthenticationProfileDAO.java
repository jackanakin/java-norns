package com.ark.norns.dao;

import com.ark.norns.entity.DeviceAuthenticationProfile;
import com.ark.norns.repository.DeviceAuthenticationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DeviceAuthenticationProfileDAO extends _DAO<DeviceAuthenticationProfile, Long> {
    @Autowired
    protected DeviceAuthenticationProfileDAO(DeviceAuthenticationProfileRepository deviceAuthenticationProfileRepository) {
        super(deviceAuthenticationProfileRepository);
    }

    public DeviceAuthenticationProfileRepository getDeviceAuthenticationProfileRepository() {
        return (DeviceAuthenticationProfileRepository) super.getBaseRepository();
    }
}
