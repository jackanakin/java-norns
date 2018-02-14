package com.ark.norns.entity;

import com.ark.norns.entity.entityView.SensorView;
import com.ark.norns.enumerated.IntervalKind;
import com.ark.norns.enumerated.SensorKind;
import com.ark.norns.enumerated.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sensor")
public class Sensor extends _Entity {
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private SensorKind sensorKind;

    private String description;

    @NotNull
    private String oid;

    @Enumerated(EnumType.STRING)
    @NotNull
    private IntervalKind interval;

    @NotNull
    private Long time;

    @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public Sensor() {
    }

    public Sensor(Long id, String name, String oid, Device device, IntervalKind interval, Status status, SensorKind sensorKind,
                  String description, Long time) {
        setId(id);
        this.name = name;
        this.oid = oid;
        this.device = device;
        this.interval = interval;
        this.status = status;
        this.sensorKind = sensorKind;
        this.description = description;
        this.time = time;
    }

    public SensorView buildView() {
        return new SensorView(getId(), getName(), getOid(), getInterval().name(), getStatus().equals(Status.ENABLED) ? true : false, getStatus().getName(),
                sensorKind == null ? null : sensorKind.name(), getDescription(), getTime(), getInterval().getShortened());
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

    public IntervalKind getInterval() {
        return interval;
    }

    public void setInterval(IntervalKind interval) {
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

    public SensorKind getSensorKind() {
        return sensorKind;
    }

    public void setSensorKind(SensorKind sensorKind) {
        this.sensorKind = sensorKind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
