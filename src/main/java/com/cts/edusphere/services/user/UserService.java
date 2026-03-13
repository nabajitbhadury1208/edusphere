package com.cts.edusphere.services.user;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    public List<User> getAllUsers();

    public User getUserById(UUID id);

    public User createUser(User user);

    public User getUserByEmail(String email);

    public void deleteUserById(UUID id);

    public User updateUserById(UUID id, UserRequestDto request, UserPrincipal userPrincipal);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    public User registerUser(RegisterRequest request);

    public void changePassword(UUID userId, String currentPassword, String newPassword);

    public User updateUserStatus(UUID id, Status status, UserPrincipal principal);
}
