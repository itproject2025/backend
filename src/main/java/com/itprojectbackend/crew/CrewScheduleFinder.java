package com.itprojectbackend.crew;

import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.crew.domain.CrewSchedule;
import com.itprojectbackend.crew.repository.CrewScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrewScheduleFinder {
    private final CrewScheduleRepository crewScheduleRepository;

    public CrewSchedule findById(long scheduleId) {
        return crewScheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new CustomException(ErrorCode.CREW_SCHEDULE_NOT_FOUND));
    }
}
