package com.ark.norns.repository;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long>, JpaSpecificationExecutor<Sensor> {
    public List<Sensor> findAllByDevice(Device device);
}
