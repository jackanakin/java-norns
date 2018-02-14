package com.ark.norns.entity;

import com.ark.norns.entity.entityView.DeviceCommunityProfileView;
import com.ark.norns.enumerated.Status;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "device_community_profile")
public class DeviceCommunityProfile extends _Entity {
    @NotNull
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public DeviceCommunityProfile() {
    }

    public DeviceCommunityProfile(Long id, String description, Status status) {
        setId(id);
        this.description = description;
        this.status = status;
    }

    public DeviceCommunityProfileView buildView() {
        return new DeviceCommunityProfileView(getId(), getDescription(),
                getStatus().equals(Status.ENABLED) ? true : false, getStatus().getName());
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
}
