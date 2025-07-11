package com.itprojectbackend.airport.controller;

import com.itprojectbackend.airport.dto.AirportResponse;
import com.itprojectbackend.airport.service.AirportService;
import com.itprojectbackend.common.domain.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/airport")
@RequiredArgsConstructor
@Tag(name = "Airport", description = "항공 관련 API 입니다.")
public class AirportController {
    private final AirportService airportService;

    @GetMapping
    @Operation(summary = "공항 코드 조회", description = "키워드를 받아 해당하는 공항 코드 리스트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<AirportResponse>>> searchAirport(@RequestParam(name = "keyword") String keyword) {
        List<AirportResponse> result = airportService.searchAirports(keyword);
        return ResponseEntity.ok(ApiResponse.success("공항 코드 조회 성공", result));
    }
}
