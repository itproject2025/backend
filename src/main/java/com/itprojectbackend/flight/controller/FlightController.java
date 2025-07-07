package com.itprojectbackend.flight.controller;

import com.itprojectbackend.flight.dto.FlightSearchRequest;
import com.itprojectbackend.flight.dto.FlightSearchResponse;
import com.itprojectbackend.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Flight", description = "비행 관련 API입니다.")
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/flights/schedule")
    @Operation(summary = "공항 스케줄 조회", description = "비행 추가 시 스케줄을 조회하는 API입니다.")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(@RequestBody FlightSearchRequest flightSearchRequest) {
        List<FlightSearchResponse> results=flightService.searchFlights(flightSearchRequest);
        return ResponseEntity.ok(results);
    }

}
