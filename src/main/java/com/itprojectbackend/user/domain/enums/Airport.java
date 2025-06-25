package com.itprojectbackend.user.domain.enums;

public enum Airport {
    ICN("Incheon International Airport"),
    GMP("Gimpo Airport"),
    PUS("Gimhae Airport"),
    CJU("Jeju International Airport"),
    YNY("Yangyang International Airport"),
    KUV("Gunsan Airport"),
    KWJ("Gwangju Airport"),
    USN("Ulsan Airport"),
    CJJ("Cheongju Airport");

    private final String displayName;

    Airport(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
