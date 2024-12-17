package com.oze.taskmanager.controller;

import static com.oze.taskmanager.constants.ApiPaths.Users.USERS;

import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.dto.UserResponseDto;
import com.oze.taskmanager.enums.Permission;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import com.oze.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(USERS)
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Delete a specific user")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Update user role")
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateUserRole(@PathVariable UUID id, @RequestParam Role role) {
        return userService.updateUserRole(id, role);
    }

    @Operation(summary = "Update user status")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateUserStatus(@PathVariable UUID id, @RequestParam Status status) {
        return userService.updateUserStatus(id, status);
    }

    @Operation(summary = "Assign permissions to a user")
    @PatchMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto assignPermissions(@PathVariable UUID id, @RequestBody Set<Permission> permissions) {
        return userService.assignPermissions(id, permissions);
    }
}
