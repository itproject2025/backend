package com.itprojectbackend.crew.controller;

import com.itprojectbackend.common.domain.ApiResponse;
import com.itprojectbackend.crew.dto.CrewScheduleRequest;
import com.itprojectbackend.crew.dto.CrewScheduleResponse;
import com.itprojectbackend.crew.dto.CrewScheduledetailResponse;
import com.itprojectbackend.crew.service.CrewScheduleService;
import com.itprojectbackend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
@Tag(name = "Crew", description = "승무원 스케줄 관련 API 입니다.")
public class CrewController {

    private final CrewScheduleService crewScheduleService;

    @GetMapping("/calendar/day")
    @Operation(summary = "일별 비행 스케줄 목록 조회", description = "해당 유저의 일별 비행 스케줄 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CrewScheduleResponse>>> getCrewSchedule(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) int year,
            @RequestParam(required = false) int month,
            @RequestParam(required = false) int day) {

        Long userId = customUserDetails.getUser().getId();
        List<CrewScheduleResponse> result = crewScheduleService.getScheduleByUserAndDay(userId, year, month, day);
        return ResponseEntity.ok(ApiResponse.success("일별 비행 스케줄 조회 성공", result));
    }

    @GetMapping("/calendar/month")
    @Operation(summary = "월별 비행 스케줄 목록 조회", description = "해당 유저의 월별 비행 스케줄 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CrewScheduleResponse>>> getMonthCrewSchedule(
            @AuthenticationPrincipal CustomUserDetails customUserDetail,
            @RequestParam(required = false) int year,
            @RequestParam(required = false) int month) {

        Long userId = customUserDetail.getUser().getId();
        List<CrewScheduleResponse> result = crewScheduleService.getScheduleByUserAndMonth(userId, year, month);
        return ResponseEntity.ok(ApiResponse.success("월별 비행 스케줄 조회 성공", result));
    }

    @PostMapping("/add/flight")
    @Operation(summary = "비행 스케줄 추가", description = "해당 유저의 비행 스케줄을 추가합니다.")
    public ResponseEntity<ApiResponse> addCrewSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @RequestBody CrewScheduleRequest crewScheduleRequest) {
        Long userId=customUserDetails.getUser().getId();
        crewScheduleService.addCrewSchedule(userId, crewScheduleRequest);
        return ResponseEntity.ok(ApiResponse.success("비행 스케줄이 성공적으로 추가되었습니다."));
    }

    @GetMapping("/flights")
    @Operation(summary = "비행 스케줄 전체 조회", description = "해당 유저의 전체 비행 스케줄을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CrewScheduleResponse>>> getCrewScheduleList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId=customUserDetails.getUser().getId();
        List<CrewScheduleResponse> scheduleList = crewScheduleService.getScheduleList(userId);
        return ResponseEntity.ok(ApiResponse.success("전체 비행 스케줄 조회 성공", scheduleList));
    }

    @DeleteMapping("/flights/{scheduleId}")
    @Operation(summary = "비행 스케줄 삭제", description = "해당 유저의 비행 스케줄을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteCrewSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @PathVariable("scheduleId") Long scheduleId){
        Long userId=customUserDetails.getUser().getId();
        crewScheduleService.deleteCrewSchedule(userId, scheduleId);
        return ResponseEntity.ok(ApiResponse.success("비행 스케줄이 삭제되었습니다."));
    }

    @GetMapping("/flights/{scheduleId}")
    @Operation(summary = "비행 스케줄 상세 조회", description = "해당 비행 스케줄을 상세 조회합니다.")
    public ResponseEntity<ApiResponse<CrewScheduledetailResponse>> getCrewScheduleDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                            @PathVariable("scheduleId") Long scheduleId){
        Long userId=customUserDetails.getUser().getId();
        CrewScheduledetailResponse detail = crewScheduleService.getScheduleDetail(userId, scheduleId);
        return ResponseEntity.ok(ApiResponse.success("비행 스케줄 상세 조회 성공", detail));
    }
}
