package com.ark.norns.entity.entityView;

import com.ark.norns.entity.Device;
import com.ark.norns.entity.Sensor;
import com.ark.norns.entity.SifCollector;
import com.ark.norns.enumerated.SNMPVersion;
import com.ark.norns.enumerated.Status;

import java.util.Set;

public class DeviceView extends _View {
    private String ipv4;
    private Integer port;
    private String description;
    private Long sifCollectorId;
    private Long authenticatioProfileId;
    private Long communityProfileId;
    private String snmp;
    private String snmp_name;
    private boolean status;
    private Set<SensorView> sensorList;

    public DeviceView() {
    }

    public DeviceView(Device device, Set<SensorView> sensorList) {
        setId(device.getId());
        this.ipv4 = device.getIpv4();
        this.port = device.getPort();
        this.snmp = device.getSnmpv().name();
        this.snmp_name = device.getSnmpv().getName();
        this.description = device.getDescription();
        this.status = device.getStatus().equals(Status.ENABLED) ? true : false;
        this.sensorList = sensorList;
        this.sifCollectorId = device.getSifCollector() != null ? device.getSifCollector().getId() : 0;
        this.authenticatioProfileId = device.getDeviceAuthenticationProfile() != null ? device.getDeviceAuthenticationProfile().getId() : 0;
        this.communityProfileId = device.getDeviceCommunityProfile() != null ? device.getDeviceCommunityProfile().getId() : 0;
    }

    public Device buildEntity() {
        return new Device(getId(), getIpv4(), getPort(), getDescription(), null,
                isStatus() ? Status.ENABLED : Status.DISABLED, SNMPVersion.valueOf(getSnmp()));
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

    public Long getSifCollectorId() {
        return sifCollectorId;
    }

    public void setSifCollectorId(Long sifCollectorId) {
        this.sifCollectorId = sifCollectorId;
    }

    public Long getAuthenticatioProfileId() {
        return authenticatioProfileId;
    }

    public void setAuthenticatioProfileId(Long authenticatioProfileId) {
        this.authenticatioProfileId = authenticatioProfileId;
    }

    public Long getCommunityProfileId() {
        return communityProfileId;
    }

    public void setCommunityProfileId(Long communityProfileId) {
        this.communityProfileId = communityProfileId;
    }
}
