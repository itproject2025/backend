package com.itprojectbackend.crew.service;

import com.itprojectbackend.crew.domain.CrewSchedule;
import com.itprojectbackend.crew.dto.CrewScheduleResponse;
import com.itprojectbackend.crew.repository.CrewScheduleRepository;
import com.itprojectbackend.flight.domain.FlightSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewScheduleService {
    private final CrewScheduleRepository crewScheduleRepository;

    public List<CrewScheduleResponse> getScheduleByUserAndDay(Long userId, int year, int month, int day) {
        LocalDate targetDate=LocalDate.of(year, month, day);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.plusDays(1).atStartOfDay().minusNanos(1);

        List<CrewSchedule> schedules=crewScheduleRepository.findByUserIdAndDate(userId, start, end);

        return schedules.stream()
                .map(cs-> {
                    FlightSchedule fs=cs.getFlightSchedule();
                    return new CrewScheduleResponse(
                            fs.getFlightNumber(),
                            fs.getDepartureCode().getCode(),
                            fs.getArrivalCode().getCode(),
                            fs.getDuration()/60,
                            fs.getDuration()%60,
                            fs.getDepartureDate(),
                            fs.getArrivalDate(),
                            cs.getFlightType()
                    );
                })
                .toList();

    }

    public List<CrewScheduleResponse> getScheduleByUserAndMonth(Long userId, int year, int month) {
        LocalDateTime start=LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime end=start.plusMonths(1).minusNanos(1);

        List<CrewSchedule> schedules=crewScheduleRepository.findByUserIdAndFlightSchedule_DepartureDateBetween(userId, start, end);

        return schedules.stream()
                .map(cs->{
                    FlightSchedule fs=cs.getFlightSchedule();
                    return new CrewScheduleResponse(
                            fs.getFlightNumber(),
                            fs.getDepartureCode().getCode(),
                            fs.getArrivalCode().getCode(),
                            fs.getDuration()/60,
                            fs.getDuration()%60,
                            fs.getDepartureDate(),
                            fs.getArrivalDate(),
                            cs.getFlightType()
                    );
                })
                .toList();
    }
}
