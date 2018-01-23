package com.ark.norns.entity.entityView;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.enumerated.Status;

import java.util.List;

public class DeviceView extends _View {
    private String ipv4;
    private Integer port;
    private String description;
    private boolean status;
    private List<SensorView> sensorList;

    public DeviceView() {
    }

    public DeviceView(Long id, String ipv4, Integer port, String description, boolean status, List<SensorView> sensorList) {
        setId(id);
        this.ipv4 = ipv4;
        this.port = port;
        this.description = description;
        this.status = status;
        this.sensorList = sensorList;
    }

    public Device buildEntity(List<Sensor> sensorList) {
        return new Device(getId(), getIpv4(), getPort(), getDescription(), sensorList,
                isStatus() ? Status.ENABLED : Status.DISABLED);
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SensorView> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<SensorView> sensorList) {
        this.sensorList = sensorList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
