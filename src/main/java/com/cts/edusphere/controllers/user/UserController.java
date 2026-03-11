package com.cts.edusphere.controllers.user;

import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.common.dto.user.UserResponseDto;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.mappers.UserMapper;
import com.cts.edusphere.services.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            var user = userService.getUserById(principal.userId());
            return ResponseEntity.ok(userMapper.toResponse(user));
        } catch (Exception e) {
            log.error("Error fetching current user details: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        try {
            var user = userService.getUserById(id);
            return ResponseEntity.ok(userMapper.toResponse(user));
        } catch (Exception e) {
            log.error("Error fetching user details: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        try {
            if (userService.getUserById(id) == null) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        try {
            var users = userService.getAllUsers();
            return ResponseEntity.ok(userMapper.toResponseList(users));
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/me")
    public ResponseEntity<UserResponseDto> updateCurrentUser(@AuthenticationPrincipal UserPrincipal principal, @RequestBody UserRequestDto request) {
        try {
            var updatedUser = userService.updateUserById(principal.userId(), request, principal);
            return ResponseEntity.ok(userMapper.toResponse(updatedUser));
        } catch (IllegalArgumentException e) {
            log.warn("Unauthorized update attempt by user {}: {}", principal.userId(), e.getMessage());
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            log.error("Error updating user details: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable UUID id, @Valid @RequestBody UserRequestDto request, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            var updatedUser = userService.updateUserById(id, request, principal);
            return ResponseEntity.ok(userMapper.toResponse(updatedUser));
        } catch (IllegalArgumentException e) {
            log.warn("Unauthorized update attempt by user {}: {}", principal.userId(), e.getMessage());
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            log.error("Error updating user details: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
