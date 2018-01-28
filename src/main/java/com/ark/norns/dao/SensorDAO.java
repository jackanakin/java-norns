package com.ark.norns.dao;

import com.ark.norns.entity.Sensor;
import com.ark.norns.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class SensorDAO extends _DAO<Sensor, Long>{
    @Autowired
    protected SensorDAO(SensorRepository sensorRepository) {
        super(sensorRepository);
    }

    public SensorRepository getSensorRepository() {
        return (SensorRepository) super.getBaseRepository();
    }

}
