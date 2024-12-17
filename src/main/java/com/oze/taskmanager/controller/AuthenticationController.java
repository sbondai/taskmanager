package com.oze.taskmanager.controller;

import static com.oze.taskmanager.constants.ApiPaths.Auth.AUTH;

import com.oze.taskmanager.dto.AuthenticationRequest;
import com.oze.taskmanager.dto.AuthenticationResponse;
import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto userRequestDto) {
        log.info("Registering user: {}", userRequestDto.getUsername());
        authenticationService.register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("User registered successfully. Waiting for admin activation.");
    }

    @Operation(summary = "Login with credentials")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        log.info("Attempting login for user: {}", request.getUsername());
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}
