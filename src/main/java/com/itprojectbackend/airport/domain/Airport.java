package com.itprojectbackend.airport.domain;


import com.itprojectbackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Airport extends BaseEntity {
    @Id
    @Column(length=3)
    private String code;

    private String name;

    private String country;

    private String flagImageUrl;
}
