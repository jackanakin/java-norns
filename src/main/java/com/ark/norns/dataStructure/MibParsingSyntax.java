package com.ark.norns.dataStructure;

public enum MibParsingSyntax {
    DisplayString(String.class),
    PhysAddress(String.class), MacAddress(String.class),
    IpAddress(String.class),

    Counter64(Long.class),
    TimeTicks(Long.class),

    INTEGER(Integer.class), Integer32(Integer.class),
    Gauge(Integer.class), Gauge32(Integer.class),
    Counter(Integer.class), Counter32(Integer.class),
    ObjectIndex(Integer.class),

    BoolValue(Boolean.class)
    ;

    private Class<?> type;

    MibParsingSyntax(Class<?> type) {
        this.type = type;
    }
}
