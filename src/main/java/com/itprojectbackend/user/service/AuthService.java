package com.itprojectbackend.user.service;

import com.itprojectbackend.airport.domain.Airport;
import com.itprojectbackend.airport.repository.AirportRepository;
import com.itprojectbackend.common.exception.CustomException;
import com.itprojectbackend.common.exception.ErrorCode;
import com.itprojectbackend.user.UserFinder;
import com.itprojectbackend.user.domain.User;
import com.itprojectbackend.user.domain.enums.Airline;
import com.itprojectbackend.user.dto.LoginRequest;
import com.itprojectbackend.user.dto.LoginResponse;
import com.itprojectbackend.user.dto.ProfileResponse;
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
    private final UserFinder userFinder;

    public void register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.email())){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_USED);
        }
        if(userRepository.existsByNickname(registerRequest.nickname())){
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_USED);
        }

        if(registerRequest.nickname().length()<2){
            throw new CustomException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }
        if(registerRequest.password().length()<6){
            throw new CustomException(ErrorCode.INVALID_PASSWORD_LENGTH);
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
                .ShowupOffsetMinutes(registerRequest.ShowupHour()*60+ registerRequest.ShowupMinute())
                .showupAlertEnabled(registerRequest.showupAlertEnabled())
                .build();
        userRepository.save(newUser);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user=userFinder.findByEmail(loginRequest.email());
        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        String token= jwtUtil.createAccessToken(user.getEmail());
        return new LoginResponse(token);
    }

    public ProfileResponse profile(Long userId) {
        User user = userFinder.findByUserId(userId);
        return ProfileResponse.from(user);
    }
}
