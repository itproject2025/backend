package com.itprojectbackend.flightdiary.service;

import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.crew.repository.CrewScheduleRepository;
import com.itprojectbackend.flight.FlightScheduleFinder;
import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flightdiary.domain.FlightDiary;
import com.itprojectbackend.flightdiary.dto.FlightDiaryRequest;
import com.itprojectbackend.flightdiary.repository.FlightDiaryRepository;
import com.itprojectbackend.user.UserFinder;
import com.itprojectbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightDiaryService {
    private final FlightDiaryRepository flightDiaryRepository;
    private final UserFinder userFinder;
    private final FlightScheduleFinder flightScheduleFinder;
    private final CrewScheduleRepository crewScheduleRepository;


    public void writeFlightDiary(Long userId, FlightDiaryRequest flightDiaryRequest) {
        User user= userFinder.findByUserId(userId);
        FlightSchedule flightSchedule=flightScheduleFinder.findByFlightScheduleId(flightDiaryRequest.flightScheduleId());

        if (!crewScheduleRepository.existsByUserAndFlightSchedule(user, flightSchedule)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CREW_SCHEDULE_ACCESS);
        }

        FlightDiary flightDiary = FlightDiary.builder()
                .content(flightDiaryRequest.content())
                .flightSchedule(flightSchedule)
                .writer(user)
                .build();

        flightDiaryRepository.save(flightDiary);
    }
}
