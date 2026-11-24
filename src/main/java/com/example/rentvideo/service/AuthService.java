package com.example.rentvideo.service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rentvideo.dto.AuthResponse;
import com.example.rentvideo.dto.LoginRequest;
import com.example.rentvideo.dto.RegisterRequest;
import com.example.rentvideo.dto.UserResponse;
import com.example.rentvideo.entity.Role;
import com.example.rentvideo.entity.User;
import com.example.rentvideo.exception.EmailAlreadyExistsException;
import com.example.rentvideo.repository.UserRepository;
import com.example.rentvideo.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        boolean isEmailPresent = userRepository.findByEmail(email).isPresent();
        if (isEmailPresent) {
            throw new EmailAlreadyExistsException("Email already present");
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRole() != null && request.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Only CUSTOMER role is allowed during registration");
        }

        user.setRole(request.getRole() == null ? Role.CUSTOMER : request.getRole());

        User savedUser = userRepository.save(user);
        UserResponse userResponse = new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getFirstName(),
                savedUser.getLastName(), savedUser.getRole());

        return userResponse;
    }

    public AuthResponse login(LoginRequest request) {

    String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            email,
            request.getPassword()
        )
    );

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    String token = jwtService.generateToken(
        user.getEmail(),
        user.getRole().name()
    );

    return new AuthResponse(token, "Bearer");
}

}
