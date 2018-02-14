package com.ark.norns.entity.entityView;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.enumerated.IntervalKind;
import com.ark.norns.enumerated.SensorKind;
import com.ark.norns.enumerated.Status;

public class SensorView extends _View {
    private String sensorKind;
    private String name;
    private String description;
    private String oid;
    private String interval;
    private String interval_shortened;
    private Long time;
    private boolean status;
    private String status_name;

    public SensorView() {
    }

    public SensorView(Long id, String name, String oid, String interval, boolean status, String status_name,
                      String sensorKind, String description, Long time, String interval_shortened) {
        setId(id);
        this.name = name;
        this.oid = oid;
        this.interval = interval;
        this.sensorKind = sensorKind;
        this.description = description;
        this.time = time;
        this.status = status;
        this.status_name = status_name;
        this.interval_shortened = interval_shortened;
    }

    public void buildView() {
        setInterval_shortened(IntervalKind.valueOf(getInterval()).getShortened());
        setStatus_name(isStatus() ? Status.ENABLED.getName() : Status.DISABLED.getName());
    }

    public Sensor buildEntity(Device device) {
        return new Sensor(getId(), getName(), getOid(), device, IntervalKind.valueOf(this.interval), isStatus() ? Status.ENABLED : Status.DISABLED,
                sensorKind == null ? null : SensorKind.valueOf(this.sensorKind), getDescription(), getTime());
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

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getSensorKind() {
        return sensorKind;
    }

    public void setSensorKind(String sensorKind) {
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

    public String getInterval_shortened() {
        return interval_shortened;
    }

    public void setInterval_shortened(String interval_shortened) {
        this.interval_shortened = interval_shortened;
    }
}
