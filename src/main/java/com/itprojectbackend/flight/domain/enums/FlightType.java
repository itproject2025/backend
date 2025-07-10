package com.itprojectbackend.flight.domain.enums;

public enum FlightType {
    QUICKTURN("Quick Turn"),
    LAYOVER("Layover");

    private final String label;

    FlightType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}