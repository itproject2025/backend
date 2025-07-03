package com.itprojectbackend.crew.dto;

import com.itprojectbackend.flight.domain.enums.FlightType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

public record CrewScheduleRequest(
        @Schema(description = "항공편 코드", example = "KE907")
        String flightIataNumber,

        @Schema(description = "출발지 공항 코드", example = "ICN")
        String departureAirportIata,

        @Schema(description = "출발 예정 시각", example = "2025-07-03T10:30:00")
        LocalDateTime departureScheduledTime,

        @Schema(description = "도착지 공항 코드", example = "LHR")
        String arrivalAirportIata,

        @Schema(description = "도착 예정 시각", example = "2025-07-03T16:00:00")
        LocalDateTime arrivalScheduledTime,

        @Schema(description = "비행 타입", example = "LAYOVER")
        FlightType flightType
) {
}
