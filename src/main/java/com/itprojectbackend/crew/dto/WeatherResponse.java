package com.itprojectbackend.crew.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record WeatherResponse (
        @Schema(description = "날씨", example = "맑음")
        String description,
        @Schema(description = "온도", example = "39.5")
        double temperature,
        @Schema(description = "강수량", example = "0.25")
        double rainfall
){
}
