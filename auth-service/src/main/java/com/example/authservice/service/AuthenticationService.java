package com.example.authservice.service;

import com.example.authservice.dto.AuthenticationResponse;
import com.example.authservice.dto.RefreshTokenRequest;
import com.example.authservice.dto.SignInRequest;
import com.example.authservice.dto.SignUpRequest;

public interface AuthenticationService {
    AuthenticationResponse signIn(SignInRequest signinRequest);

    SignUpRequest signUp(SignUpRequest signUpRequest);
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
