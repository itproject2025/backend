package com.itprojectbackend.airport.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AirportResponse(
        String code,

        String name,

        @Schema(description = "국기 URL")
        String FlagUrl
) {
}
