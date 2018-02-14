package com.ark.norns.enumerated;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SNMPVersionSerializer.class)
public enum SNMPVersion {
    V1("SNMPv1"), V2("SNMPv2"), V3("SNMPv3");

    private String name;

    SNMPVersion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
