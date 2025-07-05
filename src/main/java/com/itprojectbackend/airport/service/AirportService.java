package com.itprojectbackend.airport.service;

import com.itprojectbackend.airport.dto.AirportResponse;
import com.itprojectbackend.airport.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;

    public List<AirportResponse> searchAirports(String keyword) {
        return airportRepository
                .findByCodeContainingIgnoreCase(keyword)
                .stream()
                .map(airport -> new AirportResponse(airport.getCode(), airport.getName()))
                .toList();
    }
}
