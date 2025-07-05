package com.itprojectbackend.flightdiary.controller;

import com.itprojectbackend.flightdiary.dto.FlightDiaryRequest;
import com.itprojectbackend.flightdiary.service.FlightDiaryService;
import com.itprojectbackend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Tag(name = "Diary", description = "비행 일기 관련 API 입니다.")
public class FlightDiaryController {
    private final FlightDiaryService flightDiaryService;

    @PostMapping
    @Operation(summary = "비행 일기 작성", description = "비행 스케줄에 대한 일기를 작성하는 API입니다.")
    public ResponseEntity<String> writeFlightDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @RequestBody FlightDiaryRequest flightDiaryRequest) {
        Long userId= customUserDetails.getUser().getId();
        flightDiaryService.writeFlightDiary(userId, flightDiaryRequest);
        return ResponseEntity.ok("비행 일기가 작성되었습니다.");
    }
}
