package com.itprojectbackend.crew.repository;

import com.itprojectbackend.crew.domain.CrewSchedule;
import com.itprojectbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CrewScheduleRepository extends JpaRepository<CrewSchedule, Long> {

    @Query("""
    SELECT cs FROM CrewSchedule cs
    WHERE cs.user.id = :userId
    AND cs.flightSchedule.departureDate BETWEEN :start AND :end
    """)
    List<CrewSchedule> findByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<CrewSchedule> findByUserIdAndFlightSchedule_DepartureDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

    List<CrewSchedule> findByUser(User user);
}
