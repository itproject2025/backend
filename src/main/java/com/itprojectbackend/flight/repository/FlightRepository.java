package com.itprojectbackend.flight.repository;

import com.itprojectbackend.flight.domain.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FlightRepository extends JpaRepository<FlightSchedule, Long> {
    boolean existsByFlightNumberAndDepartureDateAndArrivalDate(String s, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
