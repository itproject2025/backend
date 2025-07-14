package com.itprojectbackend.crew.dto;

import com.itprojectbackend.flight.domain.enums.FlightType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CrewScheduledetailResponse(
        @Schema(description = "스케줄 아이디", example = "1")
        Long scheduleId,

        @Schema(description = "비행 편명", example = "KE2025")
        String flightNumber,

        @Schema(description = "출발 공항 코드", example = "GMP")
        String departureAirport,

        @Schema(description = "도착 공항 코드", example = "CJU")
        String arrivalAirport,

        @Schema(description = "지속 시간", example = "1m 30s")
        String duration,

        @Schema(description = "출발 시간", example = "2025-06-27T08:00:00")
        LocalDateTime departureDate,

        @Schema(description = "도착 시간", example = "2025-06-27T09:20:00")
        LocalDateTime arrivalDate,

        @Schema(description = "비행 타입", example = "STANDBY")
        FlightType flightType,

        @Schema(description = "출발 국가", example = "KR")
        String departureCountry,

        @Schema(description = "도착 국가", example = "JP")
        String arrivalCountry,

        @Schema(description = "출발 국가 국기 URL")
        String departureFlagUrl,

        @Schema(description = "도착 국가 국기 URL")
        String arrivalFlagUrl,


        WeatherResponse DepartureWeather,

        WeatherResponse arrivalWeather

) {
}
