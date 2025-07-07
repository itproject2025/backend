package com.itprojectbackend.flightdiary.service;

import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.crew.repository.CrewScheduleRepository;
import com.itprojectbackend.flight.FlightScheduleFinder;
import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flight.domain.enums.FlightType;
import com.itprojectbackend.flightdiary.FlightDiaryFinder;
import com.itprojectbackend.flightdiary.domain.FlightDiary;
import com.itprojectbackend.flightdiary.dto.FlightDiaryDetailResponse;
import com.itprojectbackend.flightdiary.dto.FlightDiaryListResponse;
import com.itprojectbackend.flightdiary.dto.FlightDiaryPatchRequest;
import com.itprojectbackend.flightdiary.dto.FlightDiaryRequest;
import com.itprojectbackend.flightdiary.repository.FlightDiaryRepository;
import com.itprojectbackend.user.UserFinder;
import com.itprojectbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightDiaryService {
    private final FlightDiaryRepository flightDiaryRepository;
    private final UserFinder userFinder;
    private final FlightDiaryFinder flightDiaryFinder;
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
                .isWritten(true)
                .build();

        flightDiaryRepository.save(flightDiary);
    }

    public List<FlightDiaryListResponse> getFlightDiaryList(Long userId) {
        User user = userFinder.findByUserId(userId);
        List<FlightDiary> flightDiaries = flightDiaryRepository.findByWriter(user);

        List<FlightDiaryListResponse> responses = new ArrayList<>();

        for (FlightDiary diary : flightDiaries) {
            FlightSchedule flightSchedule = diary.getFlightSchedule();

            FlightType flightType = flightSchedule.getCrewSchedules().stream()
                    .filter(crewSchedule -> crewSchedule.getUser().equals(user))
                    .findFirst()
                    .map(crewSchedule -> crewSchedule.getFlightType())
                    .orElse(null);

            responses.add(FlightDiaryListResponse.builder()
                    .diaryId(diary.getId())
                    .flightDate(String.valueOf(flightSchedule.getDepartureDate()))
                    .departureCode(flightSchedule.getDepartureCode().getCode())
                    .arrivalCode(flightSchedule.getArrivalCode().getCode())
                    .flightNumber(flightSchedule.getFlightNumber())
                    .flightType(flightType)
                    .isWritten(diary.isWritten())
                    .build());
        }

        return responses;
    }

    public FlightDiaryDetailResponse getFlightDiaryDetail(Long userId, Long diaryId) {
        User user = userFinder.findByUserId(userId);
        FlightDiary flightDiary = flightDiaryRepository.findById(diaryId).orElse(null);
        FlightSchedule flightSchedule = flightScheduleFinder.findByFlightScheduleId(flightDiary.getFlightSchedule().getId());

        FlightType flightType = flightSchedule.getCrewSchedules().stream()
                .filter(crewSchedule -> crewSchedule.getUser().equals(user))
                .findFirst()
                .map(crewSchedule -> crewSchedule.getFlightType())
                .orElse(null);


        FlightDiaryDetailResponse flightDiaryDetailResponse=FlightDiaryDetailResponse.builder()
                .diaryId(flightDiary.getId())
                .departureCode(flightSchedule.getDepartureCode().getCode())
                .departureFullName(flightSchedule.getDepartureCode().getName())
                .flightNumber(flightSchedule.getFlightNumber())
                .flightType(flightType)
                .flightDate(flightSchedule.getDepartureDate())
                .duration(formatDuration(flightSchedule.getDepartureDate(),flightSchedule.getArrivalDate()))
                .authorName(flightDiary.getWriter().getNickname())
                .content(flightDiary.getContent())
                .build();

        return flightDiaryDetailResponse;
    }

    public String formatDuration(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        Duration duration = Duration.between(departureTime, arrivalTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%dh %02dm", hours, minutes);
    }

    @Transactional
    public void patchFlightDiary(Long userId, Long diaryId, FlightDiaryPatchRequest flightDiaryPatchRequest) {
        FlightDiary flightDiary = flightDiaryFinder.findByDiaryId(diaryId);
        if(!flightDiary.getWriter().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DIARY_ACCESS);
        }
        flightDiary.update(flightDiaryPatchRequest.content());
    }

    @Transactional
    public void deleteFlightDiary(Long userId, Long diaryId) {
        FlightDiary flightDiary = flightDiaryFinder.findByDiaryId(diaryId);
        if(!flightDiary.getWriter().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DIARY_ACCESS);
        }
        flightDiaryRepository.delete(flightDiary);
    }

    public List<FlightDiaryListResponse> getUnwrittenFlightDiaryList(Long userId) {
        User user = userFinder.findByUserId(userId);
        Pageable pageable = PageRequest.of(0,5);
        List<FlightDiary> flightDiaries = flightDiaryRepository.findUnwrittenDiaries(userId, LocalDateTime.now(),pageable);

        List<FlightDiaryListResponse> responses = new ArrayList<>();

        for (FlightDiary diary : flightDiaries) {
            FlightSchedule flightSchedule = diary.getFlightSchedule();

            FlightType flightType = flightSchedule.getCrewSchedules().stream()
                    .filter(crewSchedule -> crewSchedule.getUser().equals(user))
                    .findFirst()
                    .map(crewSchedule -> crewSchedule.getFlightType())
                    .orElse(null);

            responses.add(FlightDiaryListResponse.builder()
                    .diaryId(diary.getId())
                    .flightDate(String.valueOf(flightSchedule.getDepartureDate()))
                    .departureCode(flightSchedule.getDepartureCode().getCode())
                    .arrivalCode(flightSchedule.getArrivalCode().getCode())
                    .flightNumber(flightSchedule.getFlightNumber())
                    .flightType(flightType)
                    .isWritten(diary.isWritten())
                    .build());
        }
        return responses;
    }
}
