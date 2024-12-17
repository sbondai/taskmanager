package com.oze.taskmanager.security;

import com.oze.taskmanager.dto.AuthenticationRequest;
import com.oze.taskmanager.dto.AuthenticationResponse;
import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.entity.User;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import com.oze.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(UserRequestDto userRequestDto) {
        userRepository.findByUsername(userRequestDto.getUsername()).ifPresent(user -> {
            throw new IllegalArgumentException("Username is already taken");
        });

        userRepository.findByEmail(userRequestDto.getEmail()).ifPresent(user -> {
            throw new IllegalArgumentException("Email is already registered");
        });

        User user = User.builder()
            .username(userRequestDto.getUsername())
            .password(passwordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail())
            .role(Role.USER)
            .status(Status.INACTIVE)
            .createdAt(LocalDateTime.now())
            .build();

        userRepository.save(user);

        String token = jwtService.generateToken(Map.of("role", user.getRole()), user.getUsername());
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(Map.of("role", user.getRole()), user.getUsername());
        return new AuthenticationResponse(token);
    }
}
