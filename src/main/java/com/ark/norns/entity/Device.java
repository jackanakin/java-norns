package com.ark.norns.entity;

import com.ark.norns.entity.entityView.DeviceView;
import com.ark.norns.entity.entityView.SensorView;
import com.ark.norns.enumerated.SNMPV;
import com.ark.norns.enumerated.Status;
import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "device")
public class Device extends _Entity {
    @NotNull
    private String ipv4;
    @NotNull
    private int port;

    @Nullable
    private String description;

    @OneToMany
    @Transient
    private Set<Sensor> sensors;

    @Enumerated(EnumType.ORDINAL)
    private SNMPV snmpv;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Status status;

    public Device() {
    }

    public Device(Long id, String ipv4, int port, String description, Set<Sensor> sensors, Status status, SNMPV snmpv) {
        setId(id);
        this.ipv4 = ipv4;
        this.port = port;
        this.description = description;
        this.status = status;
        this.sensors = sensors;
        this.snmpv = snmpv;
    }

    public DeviceView buildView(Set<SensorView> sensorViewList) {
        return new DeviceView(getId(), getIpv4(), getPort(), snmpv.name(), snmpv.getName(), getDescription(),
                getStatus().equals(Status.ENABLED) ? true : false, sensorViewList);
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

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public SNMPV getSnmpv() {
        return snmpv;
    }

    public void setSnmpv(SNMPV snmpv) {
        this.snmpv = snmpv;
    }
}
