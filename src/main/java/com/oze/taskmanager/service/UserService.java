package com.oze.taskmanager.service;

import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.dto.UserResponseDto;
import com.oze.taskmanager.entity.User;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import com.oze.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        userRepository.findByUsername(userRequestDto.getUsername()).ifPresent(user -> {
            throw new IllegalArgumentException("Username is already taken");
        });

        userRepository.findByEmail(userRequestDto.getEmail()).ifPresent(user -> {
            throw new IllegalArgumentException("Email is already registered");
        });

        User user = User.builder()
            .username(userRequestDto.getUsername())
            .password(passwordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail())
            .role(Role.USER)
            .status(Status.INACTIVE)
            .createdAt(LocalDateTime.now())
            .build();

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> modelMapper.map(user, UserResponseDto.class))
            .collect(Collectors.toList());
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public UserResponseDto updateUserRole(UUID id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }
}
