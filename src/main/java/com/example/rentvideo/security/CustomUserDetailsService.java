package com.example.rentvideo.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import  com.example.rentvideo.dto.*;
import com.example.rentvideo.entity.User;
import com.example.rentvideo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service 
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                        "User not found with email: " + username
                    )
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        
    }
}
