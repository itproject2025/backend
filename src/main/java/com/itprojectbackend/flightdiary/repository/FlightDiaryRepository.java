package com.itprojectbackend.flightdiary.repository;

import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flightdiary.domain.FlightDiary;
import com.itprojectbackend.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightDiaryRepository extends JpaRepository<FlightDiary, Long> {
    List<FlightDiary> findByWriter(User writer);

    FlightDiary findByFlightSchedule(FlightSchedule flightSchedule);

    Optional<FlightDiary> findById(Long diaryId);

    @Query("""
 SELECT d FROM FlightDiary d 
 WHERE d.writer.id= :userId
 AND d.isWritten = false
 ANd d.flightSchedule.departureDate < :today
 ORDER By d.flightSchedule.departureDate DESC""")
    List<FlightDiary> findUnwrittenDiaries(@Param("userId") Long userId,
                                           @Param("today")LocalDateTime today,
                                           Pageable pageable);
}
