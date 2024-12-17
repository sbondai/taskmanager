package com.oze.taskmanager.controller;

import static com.oze.taskmanager.constants.ApiPaths.Auth.AUTH;

import com.oze.taskmanager.dto.AuthenticationRequest;
import com.oze.taskmanager.dto.AuthenticationResponse;
import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequestDto userRequestDto) {
        log.info("Registering user: {}", userRequestDto.getUsername());
        AuthenticationResponse response = authenticationService.register(userRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        log.info("Attempting login for user: {}", request.getUsername());
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}
