package com.itprojectbackend.flight;

import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightScheduleFinder {
    private final FlightRepository flightRepository;

    public FlightSchedule findByFlightScheduleId(long flightScheduleId) {
        return flightRepository.findById(flightScheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_SCHEDULE_NOT_FOUND));
    }
}
