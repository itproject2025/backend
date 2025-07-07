package com.itprojectbackend.flightdiary.repository;

import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flightdiary.domain.FlightDiary;
import com.itprojectbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightDiaryRepository extends JpaRepository<FlightDiary, Long> {
    List<FlightDiary> findByWriter(User writer);

    FlightDiary findByFlightSchedule(FlightSchedule flightSchedule);

    Optional<FlightDiary> findById(Long diaryId);
}
