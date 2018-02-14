package com.ark.norns.entity.entityView;

import com.ark.norns.entity.SifCollector;
import com.ark.norns.enumerated.Status;

public class SifCollectorView extends _View {
    private String identifier;
    private String description;
    private String databaseUrl;
    private boolean status;
    private String status_name;

    public SifCollectorView() {
    }

    public SifCollectorView(Long id, String identifier, String description, String databaseUrl, boolean status, String status_name) {
        setId(id);
        this.identifier = identifier;
        this.description = description;
        this.databaseUrl = databaseUrl;
        this.status = status;
        this.status_name = status_name;
    }

    public SifCollector buildEntity() {
        return new SifCollector(getId(), getIdentifier(), getDescription(), getDatabaseUrl(), isStatus() ? Status.ENABLED : Status.DISABLED);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
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
