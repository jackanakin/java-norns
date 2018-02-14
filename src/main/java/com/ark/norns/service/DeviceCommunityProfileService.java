package com.ark.norns.service;

import com.ark.norns.dao.DeviceCommunityProfileDAO;
import com.ark.norns.entity.DeviceCommunityProfile;
import com.ark.norns.entity.entityValidator.DeviceCommunityProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceCommunityProfileService {
    @Autowired
    private DeviceCommunityProfileValidator deviceCommunityProfileValidator;
    @Autowired
    private DeviceCommunityProfileDAO deviceCommunityProfileDAO;

    public void removeEntity(Long id) {
        getDeviceCommunityProfileDAO().delete(getDeviceCommunityProfileDAO().findOne(id));
    }

    public DeviceCommunityProfile persistEntity(DeviceCommunityProfile deviceCommunityProfile) {
        return getDeviceCommunityProfileDAO().save(deviceCommunityProfile);
    }

    public DeviceCommunityProfileValidator getDeviceCommunityProfileValidator() {
        return deviceCommunityProfileValidator;
    }

    public DeviceCommunityProfileDAO getDeviceCommunityProfileDAO() {
        return deviceCommunityProfileDAO;
    }
}
