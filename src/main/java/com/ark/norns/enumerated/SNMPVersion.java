package com.ark.norns.enumerated;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SNMPVersionSerializer.class)
public enum SNMPVersion {
    V1("SNMPv1", 1), V2("SNMPv2", 2), V3("SNMPv3", 3);

    private String name;
    private int version;

    SNMPVersion(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }
}
