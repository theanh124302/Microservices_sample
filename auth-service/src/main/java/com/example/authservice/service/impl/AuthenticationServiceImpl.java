package com.example.authservice.service.impl;


import com.example.authservice.dto.AuthenticationResponse;
import com.example.authservice.dto.RefreshTokenRequest;
import com.example.authservice.dto.SignInRequest;
import com.example.authservice.dto.SignUpRequest;
import com.example.authservice.entity.User;
import com.example.authservice.exception.UsernameAlreadyExistsException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
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
        System.out.println(request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwt);
        authenticationResponse.setRefreshToken(refreshToken);
        return authenticationResponse;
    }

    @Transactional
    @Override
    public User signUp(SignUpRequest request) {
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhoneNumber(request.getPhoneNumber());
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Xử lý ngoại lệ khi username đã tồn tại trong cơ sở dữ liệu
            throw new UsernameAlreadyExistsException("Username is already taken.");
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác nếu có
            throw new ServiceException("An error occurred while signing up.", e);
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String username = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByUsername(username).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt = jwtService.generateToken(user);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwt);
            authenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return authenticationResponse;
        }
        return null;
    }
}
