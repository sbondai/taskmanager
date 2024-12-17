package com.oze.taskmanager.controller;

import static com.oze.taskmanager.constants.ApiPaths.Auth.AUTH;

import com.oze.taskmanager.dto.AuthenticationRequest;
import com.oze.taskmanager.dto.AuthenticationResponse;
import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.security.AuthenticationService;
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
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto userRequestDto) {
        log.info("Registering user: {}", userRequestDto.getUsername());
        authenticationService.register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("User registered successfully. Waiting for admin activation.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        log.info("Attempting login for user: {}", request.getUsername());
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}
