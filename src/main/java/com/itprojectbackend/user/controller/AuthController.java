package com.itprojectbackend.user.controller;

import com.itprojectbackend.user.dto.LoginRequest;
import com.itprojectbackend.user.dto.LoginResponse;
import com.itprojectbackend.user.dto.RegisterRequest;
import com.itprojectbackend.user.jwt.JwtUtil;
import com.itprojectbackend.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "User", description = "이메일 로그인 관련 API입니다.")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/users/register")
    @Operation(summary = "일반 회원 가입", description = "이메일, 비밀번호로 회원가입하는 API입니다.")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/auth/login")
    @Operation(summary = "일반 로그인", description = "이메일, 비밀번호를 받아 로그인하는 API입니다.")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse= authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
