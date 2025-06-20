package com.itprojectbackend.user.domain.enums;

public enum Airport {
    ICN("인천국제공항"),
    GMP("김포공항"),
    PUS("김해공항"),
    CJU("제주국제공항"),
    YNY("양양국제공항"),
    KUV("군산공항"),
    KWJ("광주공항"),
    USN("울산공항"),
    CJJ("청주공항");

    private final String displayName;

    Airport(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
