package com.oze.taskmanager.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminIntegrationTest extends BaseIntegrationTest {

    private UUID testUserId;

    @BeforeEach
    void setUp() {
        setupTestUsers();
        testUserId = userRepository.findByUsername("testuser").orElseThrow().getId();
    }

    @Test
    void testAdminOperations() throws Exception {
        // Activate User
        mockMvc.perform(patch("/api/v1/users/{userId}/status", testUserId)
                .param("status", "ACTIVE")
                .header("Authorization", adminToken))
            .andExpect(status().isOk());

        // Assign Permissions
        mockMvc.perform(patch("/api/v1/users/{userId}/permissions", testUserId)
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"READ_TASKS\", \"CREATE_TASKS\"]"))
            .andExpect(status().isOk());

        // Update User Role
        mockMvc.perform(patch("/api/v1/users/{userId}/role", testUserId)
                .param("role", "ADMIN")
                .header("Authorization", adminToken))
            .andExpect(status().isOk());

        // Get All Users
        mockMvc.perform(get("/api/v1/users")
                .header("Authorization", adminToken))
            .andExpect(status().isOk());
    }
}
