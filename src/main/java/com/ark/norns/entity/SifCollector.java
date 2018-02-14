package com.ark.norns.entity;

import com.ark.norns.entity.entityView.SifCollectorView;
import com.ark.norns.enumerated.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "sif_collector")
public class SifCollector extends _Entity {
    @NotNull
    private String identifier;

    private String description;

    @Column(name = "database_url")
    private String databaseUrl;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public SifCollector() {
    }

    public SifCollector(Long id, String identifier, String description, String databaseUrl, Status status) {
        setId(id);
        this.identifier = identifier;
        this.description = description;
        this.databaseUrl = databaseUrl;
        this.status = status;
    }

    public SifCollectorView buildView() {
        return new SifCollectorView(getId(), getIdentifier(), getDescription(), getDatabaseUrl(),
                getStatus().equals(Status.ENABLED) ? true : false, getStatus().getName());
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
