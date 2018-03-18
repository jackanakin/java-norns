package com.ark.norns.dataStructure;

public enum MibParsingAccess {
    RO("read-only");

    private String name;

    MibParsingAccess(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
