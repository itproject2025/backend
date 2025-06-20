package com.itprojectbackend.flight.domain;

import com.itprojectbackend.common.domain.BaseEntity;
import com.itprojectbackend.crew.domain.CrewSchedule;
import com.itprojectbackend.user.domain.enums.Airport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_schedule_id")
    private Long id;;

    private String flightNumber;

    private Airport departureCode;

    private Airport arrivalCode;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private int duration;

    private boolean isChecked;

    @OneToMany(mappedBy = "flightSchedule")
    private List<CrewSchedule> crewSchedules;
}
