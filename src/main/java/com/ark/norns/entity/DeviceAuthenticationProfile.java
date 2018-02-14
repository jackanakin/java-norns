package com.ark.norns.entity;

import com.ark.norns.entity.entityView.DeviceAuthenticationProfileView;
import com.ark.norns.enumerated.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "device_authentication_profile")
public class DeviceAuthenticationProfile extends _Entity {
    @NotNull
    private String description;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public DeviceAuthenticationProfile() {
    }

    public DeviceAuthenticationProfile(Long id, String description, String username, String password, Status status) {
        setId(id);
        this.username = username;
        this.description = description;
        this.password = password;
        this.status = status;
    }

    public DeviceAuthenticationProfileView buildView() {
        return new DeviceAuthenticationProfileView(getId(), getDescription(), getUsername(), getPassword(),
                getStatus().equals(Status.ENABLED) ? true : false, getStatus().getName());
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
