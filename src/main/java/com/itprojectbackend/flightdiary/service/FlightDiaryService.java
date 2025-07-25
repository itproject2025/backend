package com.itprojectbackend.flightdiary.service;

import com.itprojectbackend.airport.domain.Airport;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        LocalDateTime now = LocalDateTime.now();

        return flightDiaries.stream()
                .filter(diary -> diary.getFlightSchedule().getArrivalDate().isBefore(now))
                .sorted(Comparator.comparing(
                        (FlightDiary diary) -> diary.getFlightSchedule().getDepartureDate()
                ).reversed())
                .map(diary -> {
                    FlightSchedule flightSchedule = diary.getFlightSchedule();
                    FlightType flightType = flightSchedule.getCrewSchedules().stream()
                            .filter(crewSchedule -> crewSchedule.getUser().equals(user))
                            .findFirst()
                            .map(crewSchedule -> crewSchedule.getFlightType())
                            .orElse(null);

                    return FlightDiaryListResponse.builder()
                            .diaryId(diary.getId())
                            .flightScheduleId(flightSchedule.getId())
                            .flightDate(String.valueOf(flightSchedule.getDepartureDate()))
                            .departureCode(flightSchedule.getDepartureCode().getCode())
                            .arrivalCode(flightSchedule.getArrivalCode().getCode())
                            .flightNumber(flightSchedule.getFlightNumber())
                            .flightType(flightType)
                            .isWritten(diary.isWritten())
                            .country(getCountry(diary))
                            .build();
                })
                .collect(Collectors.toList());
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
                .Code(getCode(flightDiary))
                .FullName(getCodeFullName(flightDiary))
                .flightNumber(flightSchedule.getFlightNumber())
                .flightType(flightType)
                .flightDate(flightSchedule.getDepartureDate())
                .duration(formatDuration(flightSchedule.getDepartureDate(),flightSchedule.getArrivalDate()))
                .authorName(flightDiary.getWriter().getNickname())
                .content(flightDiary.getContent())
                .country(getCountry(flightDiary))
                .build();

        return flightDiaryDetailResponse;
    }

    private String getCode(FlightDiary flightDiary) {
        String baseAirport = flightDiary.getWriter().getBaseAirport().getCode();
        FlightSchedule flightSchedule = flightDiary.getFlightSchedule();
        String departureCode=flightSchedule.getDepartureCode().getCode();
        String arrivalCode=flightSchedule.getArrivalCode().getCode();

        if(baseAirport.equals(departureCode)){
            return flightSchedule.getArrivalCode().getCode();
        }else if(baseAirport.equals(arrivalCode)){
            return flightSchedule.getDepartureCode().getCode();
        }else{
            return flightSchedule.getArrivalCode().getCode();
        }
    }

    private String getCodeFullName(FlightDiary flightDiary) {
        String baseAirport = flightDiary.getWriter().getBaseAirport().getCode();
        FlightSchedule flightSchedule = flightDiary.getFlightSchedule();
        String departureCode = flightSchedule.getDepartureCode().getCode();
        String arrivalCode = flightSchedule.getArrivalCode().getCode();

        if (baseAirport.equals(departureCode)) {
            return flightSchedule.getArrivalCode().getName();
        } else if (baseAirport.equals(arrivalCode)) {
            return flightSchedule.getDepartureCode().getName();
        } else {
            return flightSchedule.getArrivalCode().getName();
        }
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

    public List<FlightDiaryListResponse> getHomeFlightDiaryList(Long userId) {
        User user = userFinder.findByUserId(userId);

        Pageable top5 = PageRequest.of(0, 5);
        Pageable top15 = PageRequest.of(0, 15);

        List<FlightDiary> unwritten = flightDiaryRepository.findTop5UnwrittenFinished(userId, top5);
        List<FlightDiary> written = flightDiaryRepository.findTop15WrittenFinished(userId, top15);

        List<FlightDiary> combined = new ArrayList<>();
        combined.addAll(unwritten);
        combined.addAll(written);

        return combined.stream().map(diary -> {
            FlightSchedule flightSchedule = diary.getFlightSchedule();

            FlightType flightType = flightSchedule.getCrewSchedules().stream()
                    .filter(crewSchedule -> crewSchedule.getUser().equals(user))
                    .findFirst()
                    .map(crewSchedule -> crewSchedule.getFlightType())
                    .orElse(null);

            return FlightDiaryListResponse.builder()
                    .diaryId(diary.getId())
                    .flightScheduleId(flightSchedule.getId())
                    .flightDate(String.valueOf(flightSchedule.getDepartureDate()))
                    .departureCode(flightSchedule.getDepartureCode().getCode())
                    .arrivalCode(flightSchedule.getArrivalCode().getCode())
                    .flightNumber(flightSchedule.getFlightNumber())
                    .flightType(flightType)
                    .isWritten(diary.isWritten())
                    .country(getCountry(diary))
                    .build();
        }).toList();
    }

    private String getCountry(FlightDiary flightDiary) {
        String baseAirport = flightDiary.getWriter().getBaseAirport().getCode();
        FlightSchedule flightSchedule = flightDiary.getFlightSchedule();
        String departureCode = flightSchedule.getDepartureCode().getCode();
        String arrivalCode = flightSchedule.getArrivalCode().getCode();

        if(baseAirport.equals(departureCode)){
            return flightSchedule.getArrivalCode().getCountry();
        } else if(baseAirport.equals(arrivalCode)){
            return flightSchedule.getDepartureCode().getCountry();
        }else{
            return flightSchedule.getArrivalCode().getCountry();
        }
    }
}
