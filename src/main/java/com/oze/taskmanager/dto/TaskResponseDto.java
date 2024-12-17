package com.oze.taskmanager.dto;

import com.oze.taskmanager.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TaskResponseDto {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
