package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public User toEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .name(request.name())
                .email(request.email())
                .role(request.role())
                .phone(request.phone())
                .status(Status.ACTIVE) // Default status for new registrations
                .build();
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus()
        );
    }

    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
