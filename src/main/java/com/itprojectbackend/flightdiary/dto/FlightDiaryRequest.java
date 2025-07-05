package com.itprojectbackend.flightdiary.dto;

public record FlightDiaryRequest(
        String content,
        Long flightScheduleId
) {
}
