package com.example.authservice.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String token;
}
