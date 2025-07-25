package com.itprojectbackend.flightdiary.dto;

import com.itprojectbackend.flight.domain.enums.FlightType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FlightDiaryDetailResponse(
        Long diaryId,
        String Code,
        String FullName,
        String flightNumber,
        FlightType flightType,
        LocalDateTime flightDate,
        String duration,
        String authorName,
        String content,
        String country
) {
}
