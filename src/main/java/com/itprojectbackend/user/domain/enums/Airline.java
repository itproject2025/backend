package com.itprojectbackend.user.domain.enums;

public enum Airline {
    KOREAN_AIR("대한항공"),
    ASIANA_AIRLINES("아시아나항공"),
    JEJU_AIR("제주항공"),
    TWAY_AIR("티웨이항공"),
    AIR_BUSAN("에어부산"),
    AIR_SEOUL("에어서울"),
    JINAIR("진에어");


    private final String displayName;

    Airline(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
