package com.oze.taskmanager.service;

import com.oze.taskmanager.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RolePermissionService {

    public Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return switch (role) {
            case ADMIN -> List.of(
                new SimpleGrantedAuthority("READ_TASKS"),
                new SimpleGrantedAuthority("CREATE_TASKS"),
                new SimpleGrantedAuthority("DELETE_TASKS")
            );
            case USER -> List.of(
                new SimpleGrantedAuthority("READ_TASKS"),
                new SimpleGrantedAuthority("CREATE_TASKS")
            );
        };
    }
}
