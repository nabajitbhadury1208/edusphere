package com.cts.edusphere.mappers.user;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserResponseDto;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;
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
                .roles(request.roles())
                .phone(request.phone())
                .status(Status.ACTIVE)
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
                user.getRoles(),
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
