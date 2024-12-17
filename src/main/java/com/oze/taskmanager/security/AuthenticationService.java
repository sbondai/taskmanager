package com.oze.taskmanager.security;

import com.oze.taskmanager.dto.AuthenticationRequest;
import com.oze.taskmanager.dto.AuthenticationResponse;
import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.entity.User;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import com.oze.taskmanager.exception.UserAlreadyExistsException;
import com.oze.taskmanager.exception.UserNotFoundException;
import com.oze.taskmanager.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRequestDto userRequestDto) {
        validateUniqueUser(userRequestDto);

        User user = User.builder()
            .username(userRequestDto.getUsername())
            .password(passwordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail())
            .role(Role.USER)
            .status(Status.INACTIVE)
            .createdAt(LocalDateTime.now())
            .build();

        userRepository.save(user);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getStatus() != Status.ACTIVE) {
            throw new BadCredentialsException("User is not activated. Please contact the admin.");
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(Map.of("role", user.getRole()), user.getUsername());
        return new AuthenticationResponse(token);
    }
    private void validateUniqueUser(UserRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is already registered");
        }
    }
}
