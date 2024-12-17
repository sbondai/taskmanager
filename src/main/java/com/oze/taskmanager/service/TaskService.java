package com.oze.taskmanager.service;

import com.oze.taskmanager.dto.TaskRequestDto;
import com.oze.taskmanager.dto.TaskResponseDto;
import com.oze.taskmanager.entity.Task;
import com.oze.taskmanager.entity.User;
import com.oze.taskmanager.enums.Permission;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.TaskStatus;
import com.oze.taskmanager.exception.UserNotFoundException;
import com.oze.taskmanager.repository.TaskRepository;
import com.oze.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskResponseDto createTask(UUID userId, TaskRequestDto request) {
        User user = validateUserAndPermission(userId, Permission.CREATE_TASKS);

        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .user(user)
            .build();

        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponseDto.class);
    }

    public List<TaskResponseDto> getTasksByUserId(UUID userId) {
        User user = validateUserAndPermission(userId, Permission.READ_TASKS);

        return taskRepository.findByUserId(user.getId()).stream()
            .map(task -> modelMapper.map(task, TaskResponseDto.class))
            .collect(Collectors.toList());
    }

    public TaskResponseDto updateTask(UUID userId, UUID taskId, TaskRequestDto request) {
        User user = validateUserAndPermission(userId, Permission.CREATE_TASKS);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Task not found or unauthorized"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskResponseDto.class);
    }

    public TaskResponseDto updateTaskStatus(UUID userId, UUID taskId, String status) {
        User user = validateUserAndPermission(userId, Permission.CREATE_TASKS);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Task not found or unauthorized"));

        task.setStatus(TaskStatus.valueOf(status.toUpperCase()));
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskResponseDto.class);
    }

    public void deleteTask(UUID userId, UUID taskId) {
        User user = validateUserAndPermission(userId, Permission.DELETE_TASKS);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Task not found or unauthorized"));

        taskRepository.delete(task);
    }

    private User validateUserAndPermission(UUID userId, Permission permission) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            return user;
        }

        Set<Permission> permissions = user.getPermissions();
        if (permissions == null || !permissions.contains(permission)) {
            throw new RuntimeException("You do not have permission: " + permission);
        }
        return user;
    }
}
