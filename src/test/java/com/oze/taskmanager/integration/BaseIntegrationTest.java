package com.oze.taskmanager.integration;

import com.oze.taskmanager.entity.User;
import com.oze.taskmanager.enums.Permission;
import com.oze.taskmanager.enums.Role;
import com.oze.taskmanager.enums.Status;
import com.oze.taskmanager.repository.UserRepository;
import com.oze.taskmanager.security.JwtService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:15:///taskmanager-db",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
public abstract class BaseIntegrationTest {

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected UserRepository userRepository;

    // Password Encoder for secure hashing
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected String adminToken;
    protected String userToken;

    @BeforeAll
    static void startContainer() {
        postgresContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        postgresContainer.stop();
    }

    protected void setupTestUsers() {
        userRepository.deleteAll();

        // Admin User with permissions
        User admin = User.builder()
            .username("admin")
            .email("admin@test.com")
            .password(passwordEncoder.encode("admin123"))
            .role(Role.ADMIN)
            .status(Status.ACTIVE)
            .permissions(Set.of(Permission.CREATE_TASKS, Permission.DELETE_TASKS))
            .createdAt(LocalDateTime.now())
            .build();
        userRepository.save(admin);

        adminToken = "Bearer " + jwtService.generateToken(Map.of("role", "ADMIN"), admin.getUsername());
        if (adminToken == null || adminToken.isEmpty()) {
            throw new IllegalStateException("Admin token generation failed!");
        }

        // Regular User with all permissions
        User user = User.builder()
            .username("testuser")
            .email("user@test.com")
            .password(passwordEncoder.encode("user123"))
            .role(Role.USER)
            .status(Status.ACTIVE)
            .permissions(Set.of(Permission.CREATE_TASKS,Permission.READ_TASKS,Permission.DELETE_TASKS))
            .createdAt(LocalDateTime.now())
            .build();
        userRepository.save(user);

        userToken = "Bearer " + jwtService.generateToken(Map.of("role", "USER"), user.getUsername());
        if (userToken == null || userToken.isEmpty()) {
            throw new IllegalStateException("User token generation failed!");
        }
    }

}
