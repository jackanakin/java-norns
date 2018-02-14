package com.ark.norns.entity.entityView;

import com.ark.norns.entity.DeviceAuthenticationProfile;
import com.ark.norns.enumerated.Status;

public class DeviceAuthenticationProfileView extends _View {
    private String description;
    private String username;
    private String password;
    private boolean status;
    private String status_name;

    public DeviceAuthenticationProfileView() {
    }

    public DeviceAuthenticationProfileView(Long id, String description, String username, String password, boolean status, String status_name) {
        setId(id);
        this.username = username;
        this.description = description;
        this.password = password;
        this.status = status;
        this.status_name = status_name;
    }

    public DeviceAuthenticationProfile buildEntity() {
        return new DeviceAuthenticationProfile(getId(), getDescription(), getUsername(), getPassword(), isStatus() ? Status.ENABLED : Status.DISABLED);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
