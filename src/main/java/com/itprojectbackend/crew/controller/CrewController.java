package com.itprojectbackend.crew.controller;

import com.itprojectbackend.crew.dto.CrewScheduleResponse;
import com.itprojectbackend.crew.service.CrewScheduleService;
import com.itprojectbackend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/crew")
@RequiredArgsConstructor
@Tag(name = "Crew", description = "승무원 스케줄 관련 API 입니다.")
public class CrewController {

    private final CrewScheduleService crewScheduleService;

    @GetMapping("/calendar")
    @Operation(summary = "일별 비행 스케줄 목록 조회", description = "해당 유저의 일별 비행 스케줄 목록를 조회합니다.")
    public ResponseEntity<List<CrewScheduleResponse>> getCrewSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @RequestParam(required = false) int year,
                                                                      @RequestParam(required = false) int month,
                                                                      @RequestParam(required = false) int day) {
        Long userId=customUserDetails.getUser().getId();
        List<CrewScheduleResponse> result=crewScheduleService.getScheduleByUserAndDay(userId, year, month, day);
        return ResponseEntity.ok(result);

    }

}
