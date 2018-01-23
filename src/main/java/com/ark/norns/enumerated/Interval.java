package com.ark.norns.enumerated;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = IntervalSerializer.class)
public enum Interval {
    SIXTY("1 MINUTO", 60), THREEHUNDRED("5 MINUTOS", 300);

    private String name;
    private int time;

    Interval(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }
}
