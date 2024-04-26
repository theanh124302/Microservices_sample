package com.example.authservice.service.impl;

import com.example.authservice.dto.*;
import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        AuthUser authUser = new AuthUser(user);
        var jwt = jwtService.generateToken(authUser);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), authUser);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwt);
        authenticationResponse.setRefreshToken(refreshToken);
        return authenticationResponse;
    }

    @Override
    public SignUpRequest signUp(SignUpRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return request;
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){

        String username = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByUsername(username).orElseThrow();
        AuthUser authUser = new AuthUser(user);
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),authUser)){
            var jwt = jwtService.generateToken(authUser);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwt);
            authenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return authenticationResponse;
        }
        return null;
    }
}