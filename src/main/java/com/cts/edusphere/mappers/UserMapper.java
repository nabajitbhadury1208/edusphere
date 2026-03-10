package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.common.dto.user.UserResponseDto;
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
                .status(Status.ACTIVE)
                .build();
    }

    public User toEntity(UserRequestDto dto) {
        if (dto == null) return null;

        return User.builder()
                .name(dto.name())
                .phone(dto.phone())
                .role(dto.role())
                .status(dto.status() != null ? dto.status() : Status.ACTIVE)
                .build();
    }

    public UserResponseDto toResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus()
        );
    }

    public List<UserResponseDto> toResponseList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
