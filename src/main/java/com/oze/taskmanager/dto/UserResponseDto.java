package com.oze.taskmanager.dto;

import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;
}
