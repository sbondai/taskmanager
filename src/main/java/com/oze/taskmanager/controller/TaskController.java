package com.oze.taskmanager.controller;

import com.oze.taskmanager.dto.TaskRequestDto;
import com.oze.taskmanager.dto.TaskResponseDto;
import com.oze.taskmanager.security.CustomUserPrincipal;
import com.oze.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.oze.taskmanager.constants.ApiPaths.Tasks.TASKS;

@RestController
@RequestMapping(TASKS)
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody TaskRequestDto request) {
        return taskService.createTask(principal.getId(), request);
    }

    @Operation(summary = "Get tasks of the authenticated user")
    @GetMapping
    public List<TaskResponseDto> getUserTasks(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return taskService.getTasksByUserId(principal.getId());
    }

    @Operation(summary = "Update a specific task")
    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody TaskRequestDto request) {
        return taskService.updateTask(principal.getId(), id, request);
    }

    @Operation(summary = "Update the status of a specific task")
    @PatchMapping("/{id}/status")
    public TaskResponseDto updateTaskStatus(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestParam String status) {
        return taskService.updateTaskStatus(principal.getId(), id, status);
    }

    @Operation(summary = "Delete a specific task")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal) {
        taskService.deleteTask(principal.getId(), id);
        return ResponseEntity.noContent().header("Message", "Task successfully deleted.").build();
    }
}
