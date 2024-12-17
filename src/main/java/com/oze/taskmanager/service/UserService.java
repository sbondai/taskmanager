package com.oze.taskmanager.service;

import com.oze.taskmanager.dto.UserRequestDto;
import com.oze.taskmanager.dto.UserResponseDto;
import com.oze.taskmanager.entity.User;
import com.oze.taskmanager.enums.Permission;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import com.oze.taskmanager.exception.UserAlreadyExistsException;
import com.oze.taskmanager.exception.UserNotFoundException;
import com.oze.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateUniqueUser(userRequestDto);

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
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDto updateUserRole(UUID id, Role role) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        user.setRole(role);
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto updateUserStatus(UUID id, Status status) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        user.setStatus(status);
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto assignPermissions(UUID id, Set<Permission> permissions) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        user.setPermissions(permissions);
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }

    private void validateUniqueUser(UserRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is already registered");
        }
    }
}
