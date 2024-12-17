package com.oze.taskmanager.controller;

import com.oze.taskmanager.dto.TaskRequestDto;
import com.oze.taskmanager.dto.TaskResponseDto;
import com.oze.taskmanager.security.CustomUserPrincipal;
import com.oze.taskmanager.service.TaskService;
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
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestBody TaskRequestDto request) {
        return taskService.createTask(principal.getId(), request);
    }

    @GetMapping
    public List<TaskResponseDto> getUserTasks(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return taskService.getTasksByUserId(principal.getId());
    }

    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestBody TaskRequestDto request) {
        return taskService.updateTask(principal.getId(), id, request);
    }

    @PatchMapping("/{id}/status")
    public TaskResponseDto updateTaskStatus(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestParam String status) {
        return taskService.updateTaskStatus(principal.getId(), id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserPrincipal principal) {
        taskService.deleteTask(principal.getId(), id);
        return ResponseEntity.ok("Task successfully deleted.");
    }
}
