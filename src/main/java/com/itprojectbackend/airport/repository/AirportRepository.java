package com.itprojectbackend.airport.repository;


import com.itprojectbackend.airport.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    Airport findByCode(String code);

}
