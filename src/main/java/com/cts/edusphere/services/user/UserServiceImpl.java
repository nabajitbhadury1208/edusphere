package com.cts.edusphere.services.user;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.*;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User updateUserById(UUID id, UserRequestDto request, UserPrincipal userPrincipal) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        boolean isAdmin = userPrincipal.authorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelfUpdate = userPrincipal.userId().equals(id);

        if (!isAdmin && !isSelfUpdate) {
            throw new InsufficientPermissionException("You do not have permission to update this user");
        }

        if (!isAdmin && (request.roles() != null || request.status() != null)) {
            throw new InsufficientPermissionException("Only administrators can modify roles or account status.");
        }


        if (request.name() != null) user.setName(request.name());
        if (request.phone() != null) user.setPhone(request.phone());

        if (isAdmin) {
            if (request.roles() != null) user.setRoles(new HashSet<>(request.roles()));
            if (request.status() != null) user.setStatus(request.status());
        }

        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User registerUser(RegisterRequest request) {

        try {
            if (userRepository.existsByEmail(request.email())) {
                throw new EmailAlreadyExistsException("Email " + request.email() + " is already in use");
            }
            User user = User.builder().name(request.name()).email(request.email()).phone(request.phone())
                    .password(passwordEncoder.encode(request.password())).roles(request.roles()).status(Status.ACTIVE)
                    .build();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UserNotCreatedException("Failed to create user: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        try {
            User user = getUserById(userId);

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new InvalidPasswordException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (Exception e) {
            throw new PasswordCannotBeChangedException("Failed to change password: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @ComplianceAudit(entityType = AuditEntityType.USER_DEACTIVATED, scope = "Verify activation status of User")
    public User updateUserStatus(UUID id, Status status, UserPrincipal principal) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (id.equals(principal.userId()) && status == Status.INACTIVE) {
            throw new IllegalArgumentException("Admins cannot deactivate their own account.");
        }


        user.setStatus(status);
        return userRepository.save(user);
    }
}
