package com.oze.taskmanager.controller;

import com.oze.taskmanager.dto.TaskRequestDto;
import com.oze.taskmanager.dto.TaskResponseDto;
import com.oze.taskmanager.security.CustomUserPrincipal;
import com.oze.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Task Management", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task", description = "Creates a task for the authenticated user")
    @ApiResponse(responseCode = "201", description = "Task created successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody TaskRequestDto request) {
        return taskService.createTask(principal.getId(), request);
    }

    @Operation(summary = "Get all tasks", description = "Retrieves all tasks for the authenticated user")
    @ApiResponse(responseCode = "200", description = "List of tasks returned successfully")
    @GetMapping
    public List<TaskResponseDto> getUserTasks(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return taskService.getTasksByUserId(principal.getId());
    }

    @Operation(summary = "Update a task", description = "Updates the specified task")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public TaskResponseDto updateTask(
        @Parameter(description = "Task ID") @PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody TaskRequestDto request) {
        return taskService.updateTask(principal.getId(), id, request);
    }

    @Operation(summary = "Update task status", description = "Updates the status of a specific task")
    @ApiResponse(responseCode = "200", description = "Task status updated successfully")
    @PatchMapping("/{id}/status")
    public TaskResponseDto updateTaskStatus(
        @Parameter(description = "Task ID") @PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestParam String status) {
        return taskService.updateTaskStatus(principal.getId(), id, status);
    }

    @Operation(summary = "Delete a task", description = "Deletes the specified task")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
        @Parameter(description = "Task ID") @PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal) {
        taskService.deleteTask(principal.getId(), id);
        return ResponseEntity.noContent().header("Message", "Task successfully deleted.").build();
    }
}
