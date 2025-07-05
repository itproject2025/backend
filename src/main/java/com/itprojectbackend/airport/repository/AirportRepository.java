package com.itprojectbackend.airport.repository;


import com.itprojectbackend.airport.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    Airport findByCode(String code);

    List<Airport> findByCodeContainingIgnoreCase(String code);

}
