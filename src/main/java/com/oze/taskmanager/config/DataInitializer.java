package com.oze.taskmanager.config;

import com.oze.taskmanager.entity.User;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import com.oze.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Optional<User> adminCheck = userRepository.findByEmail("admin@taskmanager.com");

        adminCheck.ifPresentOrElse(admin -> {
            System.out.println("Admin user already exists.");
        }, () -> {
            User adminUser = User.builder()
                .username("admin")
                .email("admin@taskmanager.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .version(0)
                .build();

            userRepository.save(adminUser);
            System.out.println("Admin user created successfully.");
        });
    }
}
