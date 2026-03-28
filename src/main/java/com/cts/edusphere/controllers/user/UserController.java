package com.cts.edusphere.controllers.user;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.common.dto.user.UserResponseDto;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.mappers.user.UserMapper;
import com.cts.edusphere.services.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing user accounts and profiles.
 * Base path: /api/v1/users
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    /**
     * Retrieves the authenticated user's own profile.
     * Accessible by any authenticated user.
     *
     * @param principal the authenticated user principal
     * @return HTTP 200 with the user's UserResponseDto, or HTTP 500 on error
     */
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

    /**
     * Retrieves a user by their unique identifier.
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the user to retrieve
     * @return HTTP 200 with the matching UserResponseDto, or HTTP 500 on error
     */
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

    /**
     * Deletes a user and nullifies their associated audit log entries.
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the user to delete
     * @return HTTP 204 on success, HTTP 404 if not found, or HTTP 500 on error
     */
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

    /**
     * Retrieves all users.
     * Accessible by ADMIN only.
     *
     * @return HTTP 200 with a list of all UserResponseDto objects, or HTTP 500 on error
     */
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

    /**
     * Updates the authenticated user's own profile.
     * Non-admin users may only update their name and phone number.
     *
     * @param principal the authenticated user principal
     * @param request the updated profile fields
     * @return HTTP 200 with the updated UserResponseDto, HTTP 403 if unauthorized, or HTTP 500 on error
     */
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

    /**
     * Updates a user by their unique identifier (admin operation).
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the user to update
     * @param request the updated user fields
     * @param principal the admin principal performing the update
     * @return HTTP 200 with the updated UserResponseDto, HTTP 403 if unauthorized, or HTTP 500 on error
     */
    @PostMapping("/{id}")
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
    /**
     * Updates the ACTIVE or INACTIVE status of a user.
     * Triggers a compliance audit. Prevents an admin from deactivating their own account.
     * Accessible by ADMIN only.
     *
     * @param id the UUID of the user whose status is being updated
     * @param requestDto the request body containing the new status
     * @param principal the admin principal performing the update
     * @return HTTP 200 with the updated UserResponseDto, or the appropriate error response
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @ComplianceAudit(entityType = AuditEntityType.STUDENT_UPDATED, scope = "Verify change of student activate status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable UUID id,
            @RequestBody UserRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            Status status = requestDto.status();
            if (status == null) {
                return ResponseEntity.badRequest().build();
            }

            var updatedUser = userService.updateUserStatus(id, status, principal);
            return ResponseEntity.ok(userMapper.toResponse(updatedUser));
        } catch (IllegalArgumentException e) {
            log.warn("Unauthorized status change: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("Error updating user status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
