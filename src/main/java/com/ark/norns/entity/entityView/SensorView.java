package com.ark.norns.entity.entityView;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.enumerated.Interval;
import com.ark.norns.enumerated.Status;

public class SensorView extends _View {
    private String name;
    private String oid;
    private String interval;
    private String interval_name;
    private boolean status;
    private String status_name;

    public SensorView() {
    }

    public SensorView(Long id, String name, String oid, String interval, String interval_name, boolean status, String status_name) {
        setId(id);
        this.name = name;
        this.oid = oid;
        this.interval = interval;
        this.interval_name = interval_name;
        this.status = status;
        this.status_name = status_name;
    }

    public void buildView() {
        setInterval_name(Interval.valueOf(interval).getName());
        setStatus_name(isStatus() ? Status.ENABLED.getName() : Status.DISABLED.getName());
    }

    public Sensor buildEntity(Device device) {
        return new Sensor(getId(), getName(), getOid(), device, Interval.valueOf(this.interval), isStatus() ? Status.ENABLED : Status.DISABLED);
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

    public String getInterval_name() {
        return interval_name;
    }

    public void setInterval_name(String interval_name) {
        this.interval_name = interval_name;
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
}
