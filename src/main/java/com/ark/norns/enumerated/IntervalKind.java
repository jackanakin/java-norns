package com.ark.norns.enumerated;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = IntervalKindSerializer.class)
public enum IntervalKind {
    PER_SECOND("POR SEGUNDO", "/seg"), PER_MINUTE("POR MINUTO", "/min"), PER_HOUR("POR HORA", "/h");

    private String name;
    private String shortened;

    IntervalKind(String name, String shortened) {
        this.name = name;
        this.shortened = shortened;
    }

    public String getName() {
        return name;
    }

    public String getShortened() {
        return shortened;
    }
}
