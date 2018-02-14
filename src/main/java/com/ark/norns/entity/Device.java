package com.ark.norns.entity;

import com.ark.norns.entity.entityView.DeviceView;
import com.ark.norns.entity.entityView.SensorView;
import com.ark.norns.enumerated.SNMPVersion;
import com.ark.norns.enumerated.Status;
import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "device")
public class Device extends _Entity {
    @NotNull
    private String ipv4;
    @NotNull
    private int port;

    @Nullable
    private String description;

    @NotNull
    @JoinColumn(name = "sif_collector_id")
    @ManyToOne
    private SifCollector sifCollector;

    @JoinColumn(name = "authentication_profile_id")
    @ManyToOne
    private DeviceAuthenticationProfile deviceAuthenticationProfile;

    @JoinColumn(name = "community_profile_id")
    @ManyToOne
    private DeviceCommunityProfile deviceCommunityProfile;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = Sensor.class)
    private Set<Sensor> sensors;

    @Enumerated(EnumType.ORDINAL)
    private SNMPVersion snmpv;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public Device() {
    }

    public Device(Long id, String ipv4, int port, String description, Set<Sensor> sensors, Status status,
                  SNMPVersion snmpv) {
        setId(id);
        this.ipv4 = ipv4;
        this.port = port;
        this.description = description;
        this.status = status;
        this.sensors = sensors;
        this.snmpv = snmpv;
    }

    public DeviceView buildView(Set<SensorView> sensorViewList) {
        return new DeviceView(this, sensorViewList);
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

    public SNMPVersion getSnmpv() {
        return snmpv;
    }

    public void setSnmpv(SNMPVersion snmpv) {
        this.snmpv = snmpv;
    }

    public SifCollector getSifCollector() {
        return sifCollector;
    }

    public void setSifCollector(SifCollector sifCollector) {
        this.sifCollector = sifCollector;
    }

    public DeviceAuthenticationProfile getDeviceAuthenticationProfile() {
        return deviceAuthenticationProfile;
    }

    public void setDeviceAuthenticationProfile(DeviceAuthenticationProfile deviceAuthenticationProfile) {
        this.deviceAuthenticationProfile = deviceAuthenticationProfile;
    }

    public DeviceCommunityProfile getDeviceCommunityProfile() {
        return deviceCommunityProfile;
    }

    public void setDeviceCommunityProfile(DeviceCommunityProfile deviceCommunityProfile) {
        this.deviceCommunityProfile = deviceCommunityProfile;
    }
}
