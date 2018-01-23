package com.ark.norns.entity;

import com.ark.norns.entity.entityView.DeviceView;
import com.ark.norns.entity.entityView.SensorView;
import com.ark.norns.enumerated.Status;
import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "device")
public class Device extends _Entity {
    @NotNull
    private String ipv4;
    @NotNull
    private int port;

    @Nullable
    private String description;

    @Transient
    private List<Sensor> sensors;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Status status;

    public Device() {
    }

    public Device(Long id, String ipv4, int port, String description, List<Sensor> sensors, Status status) {
        setId(id);
        this.ipv4 = ipv4;
        this.port = port;
        this.description = description;
        this.status = status;
        this.sensors = sensors;
    }

    public DeviceView buildView(List<SensorView> sensorViewList) {
        return new DeviceView(getId(), getIpv4(), getPort(), getDescription(), getStatus().equals(Status.ENABLED) ? true : false, sensorViewList);
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
