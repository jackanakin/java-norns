package com.ark.norns.service;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.entity.entityValidator.SensorValidator;
import com.ark.norns.entity.entityView.SensorView;
import com.ark.norns.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SensorService extends _Service<Sensor, Long> {
    @Autowired
    private SensorValidator sensorValidator;
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    protected SensorService(SensorRepository sensorRepository) {
        super(sensorRepository);
    }

    public List<Sensor> buildEntityList(List<SensorView> sensorViewList, Device device) {
        List<Sensor> sensorList = new ArrayList<>();
        for (SensorView sensorView : sensorViewList) {
            sensorList.add(sensorView.buildEntity(device));
        }
        return sensorList;
    }

    public List<SensorView> buildViewList(List<Sensor> sensorList) {
        if (sensorList == null) {
            return null;
        } else {
            List<SensorView> sensorViewList = new ArrayList<>();
            for (Sensor sensor : sensorList) {
                sensorViewList.add(sensor.buildView());
            }
            return sensorViewList;
        }
    }

    public SensorValidator getSensorValidator() {
        return this.sensorValidator;
    }

    public SensorRepository getSensorRepository() {
        return (SensorRepository) super.getBaseRepository();
    }

}
