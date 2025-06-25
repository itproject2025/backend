package com.itprojectbackend.user.domain.enums;

public enum Airline {
    KOREAN_AIR("Korean Air"),
    ASIANA_AIRLINES("Asiana Airlines"),
    JEJU_AIR("Jeju Air"),
    TWAY_AIR("T'way Air"),
    AIR_BUSAN("Air Busan"),
    AIR_SEOUL("Air Seoul"),
    JINAIR("Jin Air");


    private final String displayName;

    Airline(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
