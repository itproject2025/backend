package com.itprojectbackend.user.controller;

import com.itprojectbackend.common.domain.ApiResponse;
import com.itprojectbackend.user.dto.LoginRequest;
import com.itprojectbackend.user.dto.LoginResponse;
import com.itprojectbackend.user.dto.ProfileResponse;
import com.itprojectbackend.user.dto.RegisterRequest;
import com.itprojectbackend.user.jwt.CustomUserDetails;
import com.itprojectbackend.user.jwt.JwtUtil;
import com.itprojectbackend.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "User", description = "이메일 로그인 관련 API입니다.")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/users/register")
    @Operation(summary = "일반 회원 가입", description = "이메일, 비밀번호로 회원가입하는 API입니다.")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @PostMapping("/auth/login")
    @Operation(summary = "일반 로그인", description = "이메일, 비밀번호를 받아 로그인하는 API입니다.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", loginResponse));
    }

    @GetMapping("/users/profile")
    @Operation(summary = "내 정보 가져오기", description = "해당 유저의 정보를 가져오는 API입니다.")
    public ResponseEntity<ApiResponse<ProfileResponse>> profile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUser().getId();
        ProfileResponse profileResponse = authService.profile(userId);
        return ResponseEntity.ok(ApiResponse.success("내 정보 가져오기", profileResponse));
    }
}
