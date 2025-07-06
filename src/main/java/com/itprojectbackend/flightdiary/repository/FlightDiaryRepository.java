package com.itprojectbackend.flightdiary.repository;

import com.itprojectbackend.flightdiary.domain.FlightDiary;
import com.itprojectbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightDiaryRepository extends JpaRepository<FlightDiary, Long> {
    List<FlightDiary> findByWriter(User writer);
}
