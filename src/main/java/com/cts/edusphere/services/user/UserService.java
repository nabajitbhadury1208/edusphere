package com.cts.edusphere.services.user;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequest;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.PasswordCannotBeChangedException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotCreatedException;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public User updateUserById(UUID id, UserRequest request, UserPrincipal userPrincipal) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        boolean isAdmin = userPrincipal.authorities().stream().anyMatch(grantedAuthority -> grantedAuthority.equals("ROLE_ADMIN"));

        boolean isSelfUpdate = userPrincipal.userId().equals(id);

        if (!isAdmin && !isSelfUpdate) {
            throw new IllegalArgumentException("You do not have permission to update this user");
        }

        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone());
        }

        if (isAdmin) {
            if (request.role() != null) {
                user.setRole(request.role());
            }
            if (request.status() != null) {
                user.setStatus(request.status());
            }
        }

        return userRepository.save(user);

    }

    boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUser(RegisterRequest request) {

        try {
            if (userRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Email already in use");
            }
            User user = User.builder().name(request.name()).email(request.email()).phone(request.phone()).password(passwordEncoder.encode(request.password())).role(request.role()).status(Status.ACTIVE).build();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UserNotCreatedException("Failed to create user: " + e.getMessage());
        }
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        try {
            User user = getUserById(userId);
            if (user == null) {
                throw new ResourceNotFoundException("User with id " + userId + " not found");
            }
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (Exception e) {
            throw new PasswordCannotBeChangedException("Failed to change password: " + e.getMessage());
        }
    }
}
