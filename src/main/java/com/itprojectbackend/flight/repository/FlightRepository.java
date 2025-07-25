package com.itprojectbackend.flight.repository;

import com.itprojectbackend.flight.domain.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<FlightSchedule, Long> {
    Optional<FlightSchedule> findByFlightNumberAndDepartureDateAndArrivalDate(
            String flightNumber,
            LocalDateTime departureDate,
            LocalDateTime arrivalDate
    );
}
