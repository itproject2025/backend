package com.itprojectbackend.user.dto;

public record RegisterRequest(
        String email,
        String password,
        String nickname,
        String airline,
        String baseAirport,
        int ShowupHour,
        int ShowupMinute
) {}
