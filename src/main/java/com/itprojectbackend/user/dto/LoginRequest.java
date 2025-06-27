package com.itprojectbackend.user.dto;

public record LoginRequest (
        String email,
        String password
){
}
