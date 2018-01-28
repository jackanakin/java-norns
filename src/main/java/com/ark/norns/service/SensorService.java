package com.ark.norns.service;

import com.ark.norns.dao.SensorDAO;
import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.entity.entityValidator.SensorValidator;
import com.ark.norns.entity.entityView.SensorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class SensorService {
    @Autowired
    private SensorValidator sensorValidator;
    @Autowired
    private SensorDAO sensorDAO;

    public Set<Sensor> buildEntityList(Set<SensorView> sensorViewList, Device device) {
        Set<Sensor> sensorList = new HashSet<>();
        for (SensorView sensorView : sensorViewList) {
            sensorList.add(sensorView.buildEntity(device));
        }
        return sensorList;
    }

    public Set<SensorView> buildViewList(Set<Sensor> sensorList) {
        if (sensorList == null) {
            return null;
        } else {
            Set<SensorView> sensorViewList = new HashSet<>();
            for (Sensor sensor : sensorList) {
                sensorViewList.add(sensor.buildView());
            }
            return sensorViewList;
        }
    }

    public SensorValidator getSensorValidator() {
        return this.sensorValidator;
    }

    public SensorDAO getSensorDAO() {
        return sensorDAO;
    }

}
