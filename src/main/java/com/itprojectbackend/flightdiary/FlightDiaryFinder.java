package com.itprojectbackend.flightdiary;

import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flight.repository.FlightRepository;
import com.itprojectbackend.flightdiary.domain.FlightDiary;
import com.itprojectbackend.flightdiary.repository.FlightDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightDiaryFinder {
    private final FlightDiaryRepository flightDiaryRepository;

    public FlightDiary findByDiaryId(long diaryId){
        return flightDiaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}

