package com.itprojectbackend.user.domain;

import com.itprojectbackend.common.domain.BaseEntity;
import com.itprojectbackend.crew.domain.CrewSchedule;
import com.itprojectbackend.user.domain.enums.Airline;
import com.itprojectbackend.user.domain.enums.Airport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Airline airline;

    @Enumerated(EnumType.STRING)
    private Airport baseAirport;

    @OneToMany(mappedBy = "user")
    private List<CrewSchedule> crewSchedules;
}
