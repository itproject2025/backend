package com.itprojectbackend.common.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {
    private final boolean isSuccess;
    private final String message;

    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }

    public static ApiResponse failure(String message) {
        return new ApiResponse(false, message);
    }
}
