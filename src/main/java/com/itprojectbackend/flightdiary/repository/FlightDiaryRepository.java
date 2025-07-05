package com.itprojectbackend.flightdiary.repository;

import com.itprojectbackend.flightdiary.domain.FlightDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDiaryRepository extends JpaRepository<FlightDiary, Long> {
}
