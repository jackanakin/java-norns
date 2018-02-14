package com.ark.norns.entity.entityView;

import com.ark.norns.entity.DeviceCommunityProfile;
import com.ark.norns.enumerated.Status;

public class DeviceCommunityProfileView extends _View {
    private String description;
    private boolean status;
    private String status_name;

    public DeviceCommunityProfileView() {
    }

    public DeviceCommunityProfileView(Long id, String description, boolean status, String status_name) {
        setId(id);
        this.description = description;
        this.status = status;
        this.status_name = status_name;
    }

    public DeviceCommunityProfile buildEntity() {
        return new DeviceCommunityProfile(getId(), getDescription(), isStatus() ? Status.ENABLED : Status.DISABLED);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
