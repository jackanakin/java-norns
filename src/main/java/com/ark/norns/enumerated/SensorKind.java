package com.ark.norns.enumerated;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SensorKindSerializer.class)
public enum SensorKind {
    THROUGHPUT("TRÁFEGO DE DADOS"), ICMP("ICMP"), UPTIME("TEMPO LIGADO");

    private String name;

    SensorKind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
