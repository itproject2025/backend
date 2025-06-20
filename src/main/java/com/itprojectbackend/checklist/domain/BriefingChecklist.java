package com.itprojectbackend.checklist.domain;

import com.itprojectbackend.common.domain.BaseEntity;
import com.itprojectbackend.flight.domain.FlightSchedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BriefingChecklist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checklistId;

    private String ItemName;

    private boolean isChecked;

    @ManyToOne
    @JoinColumn(name = "flight_schedule_id")
    private FlightSchedule flightSchedule;

}
