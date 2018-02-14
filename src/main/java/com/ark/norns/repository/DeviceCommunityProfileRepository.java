package com.ark.norns.repository;

import com.ark.norns.entity.DeviceCommunityProfile;
import com.ark.norns.enumerated.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Iterator;

public interface DeviceCommunityProfileRepository extends JpaRepository<DeviceCommunityProfile, Long>, JpaSpecificationExecutor<DeviceCommunityProfile> {
    public Iterator<DeviceCommunityProfile> findAllByStatusOrderByDescriptionAsc(Status status);
}
