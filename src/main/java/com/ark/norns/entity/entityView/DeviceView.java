package com.ark.norns.entity.entityView;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.enumerated.SNMPV;
import com.ark.norns.enumerated.Status;

import java.util.Set;

public class DeviceView extends _View {
    private String ipv4;
    private Integer port;
    private String snmp;
    private String snmp_name;
    private String description;
    private boolean status;
    private Set<SensorView> sensorList;

    public DeviceView() {
    }

    public DeviceView(Long id, String ipv4, Integer port, String snmp, String snmp_name, String description, boolean status, Set<SensorView> sensorList) {
        setId(id);
        this.ipv4 = ipv4;
        this.port = port;
        this.snmp = snmp;
        this.snmp_name = snmp_name;
        this.description = description;
        this.status = status;
        this.sensorList = sensorList;
    }

    public Device buildEntity(Set<Sensor> sensorList) {
        return new Device(getId(), getIpv4(), getPort(), getDescription(), sensorList,
                isStatus() ? Status.ENABLED : Status.DISABLED, SNMPV.valueOf(getSnmp()));
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

    public Set<SensorView> getSensorList() {
        return sensorList;
    }

    public void setSensorList(Set<SensorView> sensorList) {
        this.sensorList = sensorList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSnmp() {
        return snmp;
    }

    public void setSnmp(String snmp) {
        this.snmp = snmp;
    }

    public String getSnmp_name() {
        return snmp_name;
    }

    public void setSnmp_name(String snmp_name) {
        this.snmp_name = snmp_name;
    }
}
