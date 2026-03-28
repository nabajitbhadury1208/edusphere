package com.cts.edusphere.mappers.user;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserResponseDto;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper component responsible for converting between {@link User} entity objects
 * and their corresponding DTO representations ({@link RegisterRequest} and
 * {@link UserResponseDto}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever user
 * mapping is required. It also provides a convenience method for mapping a collection
 * of users to a list of response DTOs.</p>
 */
@Component
public class UserMapper {

    /**
     * Converts a {@link RegisterRequest} DTO to a {@link User} entity.
     *
     * <p>If the provided request is {@code null}, this method returns {@code null}.
     * The user's status is defaulted to {@link Status#ACTIVE} during creation;
     * the password encoding must be applied separately before persisting the entity.</p>
     *
     * @param request the {@link RegisterRequest} DTO containing registration data;
     *                may be {@code null}
     * @return a new {@link User} entity built from the request data,
     *         or {@code null} if the input is {@code null}
     */
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

    /**
     * Converts a {@link User} entity to a {@link UserResponseDto}.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * Sensitive fields such as the password are intentionally excluded from the response.</p>
     *
     * @param user the {@link User} entity to convert; may be {@code null}
     * @return a {@link UserResponseDto} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
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

    /**
     * Converts a list of {@link User} entities to a list of {@link UserResponseDto} objects.
     *
     * <p>If the provided list is {@code null} or empty, an empty list is returned.
     * Each user in the list is converted using {@link #toResponse(User)}.</p>
     *
     * @param users the list of {@link User} entities to convert; may be {@code null} or empty
     * @return a list of {@link UserResponseDto} objects, or an empty list if the input
     *         is {@code null} or empty
     */
    public List<UserResponseDto> toResponseList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
