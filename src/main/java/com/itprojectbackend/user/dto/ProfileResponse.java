package com.itprojectbackend.user.dto;

import com.itprojectbackend.user.domain.User;
import org.springframework.context.annotation.Profile;

public record ProfileResponse(
        String email,
        String nickname,
        String airline,
        String baseAirportCode,
        String baseAirportName,
        int showupHour,
        int showupMinute,
        boolean showupAlertEnabled
) {
    public static ProfileResponse from(User user) {
        return new ProfileResponse(
                user.getEmail(),
                user.getNickname(),
                user.getAirline().name(),
                user.getBaseAirport().getCode(),
                user.getBaseAirport().getName(),
                user.getShowupOffsetMinutes()/60,
                user.getShowupOffsetMinutes()%60,
                user.isShowupAlertEnabled()
        );
    }
}
