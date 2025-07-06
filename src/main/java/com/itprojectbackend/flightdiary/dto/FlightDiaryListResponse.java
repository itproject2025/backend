package com.itprojectbackend.flightdiary.dto;

import com.itprojectbackend.flight.domain.enums.FlightType;
import lombok.Builder;

@Builder
public record FlightDiaryListResponse(
        Long diaryId,
        String flightDate,
        String departureCode,
        String arrivalCode,
        String flightNumber,
        FlightType flightType,
        boolean isWritten
){

}
