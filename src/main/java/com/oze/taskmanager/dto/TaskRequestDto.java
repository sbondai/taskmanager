package com.oze.taskmanager.dto;

import com.oze.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private TaskStatus status;
}
