package com.itprojectbackend.flight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FlightSearchResponse (
        @Schema(description = "항공편 코드", example = "KE907")
        String flightIataNumber,

        @Schema(description = "출발지 공항 코드", example = "ICN")
        String departureAirportIata,

        @Schema(description = "출발 예정 시각", example = "2025-07-03T10:30:00")
        String departureScheduledTime,

        @Schema(description = "출발지 공항 코드", example = "LHR")
        String arrivalAirportIata,

        @Schema(description = "도착 예정 시각", example = "2025-07-03T10:30:00")
        String arrivalScheduledTime,

        @Schema(description = "항공사 이름", example = "KE")
        String airlineIataCode

) {}