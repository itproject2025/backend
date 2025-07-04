package com.itprojectbackend.flight.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record FlightSearchRequest (
        @Schema(description = "출발지 공항 코드", example = "ICN")
        String departure,

        @Schema(description = "도착지 공항 코드", example = "LHR")
        String arrival,

        @Schema(description = "출발날짜", example = "2025-07-03")
        String date,

        @Schema(description = "항공사 코드", example = "KE")
        String iata
){
}
