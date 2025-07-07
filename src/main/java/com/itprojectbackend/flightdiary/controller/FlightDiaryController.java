package com.itprojectbackend.flightdiary.controller;

import com.itprojectbackend.flightdiary.dto.FlightDiaryDetailResponse;
import com.itprojectbackend.flightdiary.dto.FlightDiaryListResponse;
import com.itprojectbackend.flightdiary.dto.FlightDiaryPatchRequest;
import com.itprojectbackend.flightdiary.dto.FlightDiaryRequest;
import com.itprojectbackend.flightdiary.service.FlightDiaryService;
import com.itprojectbackend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @Operation(summary = "비행 일기 조회", description = "해당 유저의 비행 일기를 조회하는 API입니다.")
    public ResponseEntity<List<FlightDiaryListResponse>> getFlightDiaryList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId= customUserDetails.getUser().getId();
        return ResponseEntity.ok(flightDiaryService.getFlightDiaryList(userId));
    }

    @GetMapping("{diaryId}")
    @Operation(summary = "비행 일기 상세 조회", description = "해당 비행을 상세 조회하는 API입니다.")
    public ResponseEntity<FlightDiaryDetailResponse> getFlightDiaryDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                          @PathVariable("diaryId") Long diaryId){
        Long userId= customUserDetails.getUser().getId();
        return ResponseEntity.ok(flightDiaryService.getFlightDiaryDetail(userId,diaryId));
    }

    @PatchMapping("{diaryId}")
    @Operation(summary = "비행 일기 수정",description = "해당 비행을 수정하는 API입니다.")
    public ResponseEntity<String> patchFlightDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @PathVariable("diaryId") Long diaryId,
                                                   @RequestBody FlightDiaryPatchRequest flightDiaryPatchRequest) {
        Long userId= customUserDetails.getUser().getId();
        flightDiaryService.patchFlightDiary(userId,diaryId,flightDiaryPatchRequest);
        return ResponseEntity.ok("해당 비행 일기가 수정되었습니다.");
    }

    @DeleteMapping("{diaryId}")
    @Operation(summary = "비행 일기 삭제",description = "해당 비행을 삭제하는 API입니다.")
    public ResponseEntity<String> deleteFlightDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                    @PathVariable("diaryId") Long diaryId) {
        Long userId= customUserDetails.getUser().getId();
        flightDiaryService.deleteFlightDiary(userId,diaryId);
        return ResponseEntity.ok("해당 비행 일기가 삭제되었습니다.");
    }

    @GetMapping("/unwritten")
    @Operation(summary = "작성하지 않은 비행일기 조회", description = "해당 유저가 작성하지 않은 비행일기 5개 조회")
    public ResponseEntity<List<FlightDiaryListResponse>> getUnwrittenFlightDiaryList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId= customUserDetails.getUser().getId();
        return ResponseEntity.ok(flightDiaryService.getUnwrittenFlightDiaryList(userId));
    }
}
