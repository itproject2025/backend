package com.itprojectbackend.crew.service;

import com.itprojectbackend.airport.domain.Airport;
import com.itprojectbackend.airport.repository.AirportRepository;
import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.crew.domain.CrewSchedule;
import com.itprojectbackend.crew.dto.CrewScheduleRequest;
import com.itprojectbackend.crew.dto.CrewScheduleResponse;
import com.itprojectbackend.crew.repository.CrewScheduleRepository;
import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flight.repository.FlightRepository;
import com.itprojectbackend.flightdiary.domain.FlightDiary;
import com.itprojectbackend.flightdiary.repository.FlightDiaryRepository;
import com.itprojectbackend.user.UserFinder;
import com.itprojectbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewScheduleService {
    private final CrewScheduleRepository crewScheduleRepository;
    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;
    private final UserFinder userFinder;
    private final FlightDiaryRepository flightDiaryRepository;

    public List<CrewScheduleResponse> getScheduleByUserAndDay(Long userId, int year, int month, int day) {
        LocalDate targetDate=LocalDate.of(year, month, day);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.plusDays(1).atStartOfDay().minusNanos(1);

        List<CrewSchedule> schedules=crewScheduleRepository.findByUserIdAndDate(userId, start, end);

        return schedules.stream()
                .map(cs-> {
                    FlightSchedule fs=cs.getFlightSchedule();
                    return new CrewScheduleResponse(
                            cs.getId(),
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
                            cs.getId(),
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

    public void addCrewSchedule(Long userId, CrewScheduleRequest crewScheduleRequest) {
        if (flightRepository.existsByFlightNumberAndDepartureDateAndArrivalDate(
                crewScheduleRequest.flightIataNumber(),
                crewScheduleRequest.departureScheduledTime(),
                crewScheduleRequest.arrivalScheduledTime())) {

            throw new CustomException(ErrorCode.FLIGHT_SCHEDULE_ALREADY_EXISTS);
        }

        User user = userFinder.findByUserId(userId);
        Airport departure = airportRepository.findByCode(crewScheduleRequest.departureAirportIata());

        if (departure == null) {
            departure = Airport.builder()
                    .code(crewScheduleRequest.departureAirportIata())
                    .build();
            airportRepository.save(departure);
        }

        Airport arrival = airportRepository.findByCode(crewScheduleRequest.arrivalAirportIata());
        if (arrival == null) {
            arrival = Airport.builder()
                    .code(crewScheduleRequest.arrivalAirportIata())
                    .build();
            airportRepository.save(arrival);
        }
        Duration duration = Duration.between(
                crewScheduleRequest.departureScheduledTime(),
                crewScheduleRequest.arrivalScheduledTime()
        );

        int durationMinutes = (int) duration.toMinutes();
        FlightSchedule flightSchedule=FlightSchedule.builder()
                .flightNumber(crewScheduleRequest.flightIataNumber())
                .departureCode(departure)
                .arrivalCode(arrival)
                .departureDate(crewScheduleRequest.departureScheduledTime())
                .arrivalDate(crewScheduleRequest.arrivalScheduledTime())
                .duration(durationMinutes)
                .build();

        flightRepository.save(flightSchedule);

        CrewSchedule crewSchedule=CrewSchedule.builder()
                .user(user)
                .flightSchedule(flightSchedule)
                .flightType(crewScheduleRequest.flightType())
                .build();
        crewScheduleRepository.save(crewSchedule);

        FlightDiary diary = FlightDiary.builder()
                .flightSchedule(flightSchedule)
                .writer(user)
                .isWritten(false)
                .build();

        flightDiaryRepository.save(diary);
    }

    public List<CrewScheduleResponse> getScheduleList(Long userId) {
        User user = userFinder.findByUserId(userId);
        List<CrewSchedule> crewSchedules=crewScheduleRepository.findByUser(user);
        return crewSchedules.stream()
                .map(schedule -> {
                    FlightSchedule flight=schedule.getFlightSchedule();

                    LocalDateTime departureTime = flight.getDepartureDate();
                    LocalDateTime arrivalTime = flight.getArrivalDate();

                    long totalMinute= Duration.between(departureTime, arrivalTime).toMinutes();
                    int durationHour = (int) (totalMinute/60);
                    int durationMinute = (int) (totalMinute%60);

                    return new CrewScheduleResponse(
                            schedule.getId(),
                            flight.getFlightNumber(),
                            flight.getDepartureCode().getCode(),
                            flight.getArrivalCode().getCode(),
                            durationHour,
                            durationMinute,
                            departureTime,
                            arrivalTime,
                            schedule.getFlightType()
                    );
                })
                .toList();
    }

    public void deleteCrewSchedule(Long userId, Long id) {
        User user=userFinder.findByUserId(userId);
        CrewSchedule crewSchedule=crewScheduleRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.CREW_SCHEDULE_NOT_FOUND));

        if(!crewSchedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CREW_SCHEDULE_ACCESS);
        }

        crewScheduleRepository.delete(crewSchedule);
    }
}
