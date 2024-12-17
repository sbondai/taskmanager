package com.oze.taskmanager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.oze.taskmanager.dto.TaskRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        setupTestUsers();
    }

    @Test
    void testTaskCRUDOperations() throws Exception {

        setupTestUsers();

        String taskCreatePayload = """
        {
            "title": "Test Task",
            "description": "Task description",
            "status": "PENDING"
        }
        """;

        MvcResult result = mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskCreatePayload))
            .andExpect(status().isCreated())
            .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String taskId = JsonPath.read(responseContent, "$.id");

        String taskUpdatePayload = """
        {
            "title": "Updated Task",
            "description": "Task description",
            "status": "IN_PROGRESS"
        }
        """;

        mockMvc.perform(put("/api/v1/tasks/{taskId}", taskId)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskUpdatePayload))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tasks")
                .header("Authorization", userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Updated Task"))
            .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));

        mockMvc.perform(delete("/api/v1/tasks/{taskId}", taskId)
                .header("Authorization", userToken))
            .andExpect(status().isNoContent())
            .andExpect(header().string("Message", "Task successfully deleted."));
    }

}
