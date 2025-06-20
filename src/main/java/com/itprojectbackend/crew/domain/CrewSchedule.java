package com.itprojectbackend.crew.domain;

import com.itprojectbackend.common.domain.BaseEntity;
import com.itprojectbackend.flight.domain.FlightSchedule;
import com.itprojectbackend.flight.domain.enums.FlightType;
import com.itprojectbackend.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrewSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="flight_schedule_id")
    private FlightSchedule flightSchedule;

    @Enumerated(EnumType.STRING)
    private FlightType flightType;

    private boolean isWorking;

    private boolean isTraining;
}
