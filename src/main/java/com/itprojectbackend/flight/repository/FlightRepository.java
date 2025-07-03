package com.itprojectbackend.flight.repository;

import com.itprojectbackend.flight.domain.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<FlightSchedule, Long> {
}
