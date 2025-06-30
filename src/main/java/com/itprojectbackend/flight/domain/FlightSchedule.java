package com.itprojectbackend.flight.domain;

import com.itprojectbackend.airport.domain.Airport;
import com.itprojectbackend.common.domain.BaseEntity;
import com.itprojectbackend.crew.domain.CrewSchedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FlightSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_schedule_id")
    private Long id;;

    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "departure_code")
    private Airport departureCode;

    @ManyToOne
    @JoinColumn(name = "arrival_code")
    private Airport arrivalCode;


    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private int duration;

    private boolean isChecked; //비행 여부

    @OneToMany(mappedBy = "flightSchedule")
    private List<CrewSchedule> crewSchedules;
}
