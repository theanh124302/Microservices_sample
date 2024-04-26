package com.example.authservice.service.impl;

import com.example.authservice.dto.AuthUser;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUsername(username).map(AuthUser::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));

            }
        };
    }
}
