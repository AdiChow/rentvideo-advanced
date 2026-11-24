
package com.example.rentvideo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.rentvideo.entity.Role;
import com.example.rentvideo.entity.User;
import com.example.rentvideo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (adminEmail == null || adminEmail.isBlank()
                || adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("Admin credentials are missing");
        }
        String email = adminEmail.trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        User admin = new User();

        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstName("System");
        admin.setLastName("Admin");
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);
    }
}
