package com.ark.norns.dao;

import com.ark.norns.entity.DeviceCommunityProfile;
import com.ark.norns.repository.DeviceCommunityProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DeviceCommunityProfileDAO extends _DAO<DeviceCommunityProfile, Long> {
    @Autowired
    protected DeviceCommunityProfileDAO(DeviceCommunityProfileRepository deviceCommunityProfileRepository) {
        super(deviceCommunityProfileRepository);
    }

    public DeviceCommunityProfileRepository getDeviceCommunityProfileRepository() {
        return (DeviceCommunityProfileRepository) super.getBaseRepository();
    }
}
