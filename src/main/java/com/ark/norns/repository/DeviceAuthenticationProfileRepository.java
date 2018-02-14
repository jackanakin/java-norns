package com.ark.norns.repository;

import com.ark.norns.entity.DeviceAuthenticationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceAuthenticationProfileRepository extends JpaRepository<DeviceAuthenticationProfile, Long>, JpaSpecificationExecutor<DeviceAuthenticationProfile> {
}
