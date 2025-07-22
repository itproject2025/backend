package com.itprojectbackend.user.domain;

import com.itprojectbackend.airport.domain.Airport;
import com.itprojectbackend.common.domain.BaseEntity;
import com.itprojectbackend.crew.domain.CrewSchedule;
import com.itprojectbackend.user.domain.enums.Airline;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    private String email;

    private String password;

    private String nickname;

    private int ShowupOffsetMinutes;

    @Enumerated(EnumType.STRING)
    private Airline airline;

    @Column(nullable = false)
    private boolean showupAlertEnabled;

    @ManyToOne
    @JoinColumn(name = "base_airport_code")
    private Airport baseAirport;

    @OneToMany(mappedBy = "user")
    private List<CrewSchedule> crewSchedules;
}
