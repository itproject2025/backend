package com.itprojectbackend.flight.domain.enums;

public enum FlightType {
    QUICK_TURN("Quick Turn"),
    LAYOVER("Layover"),
    DEADHEADING("Deadheading"),
    STANDBY("Standby"),
    TRAINING("Training");

    private final String label;

    FlightType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}