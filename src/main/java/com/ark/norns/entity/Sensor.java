package com.ark.norns.entity;

import com.ark.norns.entity.entityView.SensorView;
import com.ark.norns.enumerated.Interval;
import com.ark.norns.enumerated.Status;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sensor")
public class Sensor extends _Entity {
    @NotNull
    private String name;
    @NotNull
    private String oid;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Interval interval;

    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Status status;

    public Sensor() {
    }

    public Sensor(Long id, String name, String oid, Device device, Interval interval, Status status) {
        setId(id);
        this.name = name;
        this.oid = oid;
        this.device = device;
        this.interval = interval;
        this.status = status;
    }

    public SensorView buildView() {
        return new SensorView(getId(), getName(), getOid(), getInterval().name(), getInterval().getName(), getStatus().equals(Status.ENABLED) ? true : false, getStatus().getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
