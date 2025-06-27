package com.itprojectbackend.user.service;

import com.itprojectbackend.airport.domain.Airport;
import com.itprojectbackend.airport.repository.AirportRepository;
import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.user.domain.User;
import com.itprojectbackend.user.domain.enums.Airline;
import com.itprojectbackend.user.dto.LoginRequest;
import com.itprojectbackend.user.dto.LoginResponse;
import com.itprojectbackend.user.dto.RegisterRequest;
import com.itprojectbackend.user.jwt.JwtUtil;
import com.itprojectbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AirportRepository airportRepository;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.email())){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        Airline airline;
        Airport airport;
        try{
            airline = Airline.valueOf(registerRequest.airline());
            airport = airportRepository.findById(registerRequest.baseAirport())
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_AIRPORT));
        } catch(IllegalArgumentException e){
            throw new CustomException(ErrorCode.INVALID_ENUM);
        }


        String encodedPassword = passwordEncoder.encode(registerRequest.password());

        User newUser = User.builder()
                .email(registerRequest.email())
                .password(encodedPassword)
                .nickname(registerRequest.nickname())
                .airline(airline)
                .baseAirport(airport)
                .build();
        userRepository.save(newUser);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user=userRepository.findByEmail(loginRequest.email())
                .orElseThrow(()->(new CustomException(ErrorCode.USER_NOT_FOUND)));
        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        String token= jwtUtil.createAccessToken(user.getEmail());
        return new LoginResponse(token);
    }
}
