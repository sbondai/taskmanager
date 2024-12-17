package com.oze.taskmanager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oze.taskmanager.dto.AuthenticationRequest;
import com.oze.taskmanager.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        setupTestUsers();
    }

    @Test
    void testUserRegistrationAndLogin() throws Exception {
        // Test Registration
        UserRequestDto userRequest = new UserRequestDto();
        userRequest.setUsername("newuser");
        userRequest.setPassword("password123");
        userRequest.setEmail("newuser@test.com");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().string("User registered successfully. Waiting for admin activation."));

        // Test Login
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("user123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", notNullValue()));
    }
}
