package com.cts.edusphere.services.user;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.*;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.audit_log.AuditLogRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } 
        
        catch (UsersNotFoundException e) {
            log.error("Error occurred while fetching all users: {}", e.getMessage());
            throw new UsersNotFoundException("Failed to fetch user records: " + e.getMessage());

        } 
        
        catch (Exception e) {
            log.error("Unexpected error occurred while fetching all users: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while retrieving user records");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
                    
        } 
        
        catch (UserNotFoundException e) {
            log.error("Error occurred while fetching user {}: {}", id, e.getMessage());
            throw new UserNotFoundException("Failed to fetch user record");

        } 
        
        catch (Exception e) {
            throw new InternalServerErrorException("Failed to retrieve user: " + e.getMessage());
        }   
    }
    
    @Override
    public User createUser(User user) {
        try {
            return userRepository.save(user);
            
        } 
        
        catch (UserCreationFailedException e) {
            log.error("Error occurred while creating user: {}", e.getMessage());
            throw new UserCreationFailedException("Failed to create user record: " + e.getMessage());

        } 
        
        catch (Exception e) {
            log.error("Unexpected error occurred while creating user: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while creating the user record");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        } 
        
        catch (UserNotFoundException e) {
            log.error("Error occurred while fetching user with email {}: {}", email, e.getMessage());
            throw new UserNotFoundException("Failed to fetch user record with email: " + email);

        } 
        
        catch (Exception e) {
            log.error("Unexpected error occurred while fetching user with email {}: {}", email, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while retrieving the user record");
        }
    }

    @Override
    @Transactional
    public void deleteUserById(UUID id) {
        try {
            if (!userRepository.existsById(id)) {
                throw new UserNotFoundException("User with id " + id + " not found");
            }
            auditLogRepository.nullifyUserOnAuditLogs(id);
            userRepository.deleteById(id);

        }
        
        catch (UserDeletionFailedException e) {
            log.error("Error occurred while deleting user {}: {}", id, e.getMessage());
            throw new UserDeletionFailedException("Failed to delete user record: " + e.getMessage());

        } 
        
        catch (Exception e) {
            log.error("Unexpected error occurred while deleting user {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while deleting the user record");
        }
    }

    @Override
    public User updateUserById(UUID id, UserRequestDto request, UserPrincipal userPrincipal) {
        try {
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
    
            if (isAdmin && request.roles() != null){
                    Set<Role> existingRoles = user.getRoles() != null
                            ? new HashSet<>(user.getRoles()) : new HashSet<>();
                    existingRoles.addAll(request.roles());
                    user.setRoles(existingRoles);
                }
            if(user.getStatus() != null) user.setStatus(request.status());

            return userRepository.save(user);
        } 
        
        catch(UserUpdateFailedException e) {
            log.error("Error occurred while updating user {}: {}", id, e.getMessage());
            throw new UserUpdateFailedException("Failed to update user record: " + e.getMessage());

        } 
        
        catch (Exception e) {
            log.error("Unexpected error occurred while updating user {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the user record");
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } 
        
        catch(EmailAlreadyExistsException e) {
            log.error("Error checking existence of email {}: {}", email, e.getMessage());
            throw new EmailAlreadyExistsException("Failed to check email existence: " + e.getMessage());
        }
        
        catch (Exception e) {
            log.error("Unexpected error occurred while checking email existence {}: {}", email, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while checking email existence");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        }
        catch(UserNotFoundException e) {
            log.error("Error occurred while finding user by email {}: {}", email, e.getMessage());
            throw new UserNotFoundException("Failed to find user record with email: " + email);
        }
        
        catch (Exception e) {
            log.error("Unexpected error occurred while finding user by email {}: {}", email, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while retrieving the user record");
        }
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
        } 

        catch(EmailAlreadyExistsException e) {
            log.error("Error occurred while registering user with email {}: {}", request.email(), e.getMessage());
            throw new EmailAlreadyExistsException("Failed to register user: " + e.getMessage());

        }
        
        catch (Exception e) {
            throw new UserCreationFailedException("Failed to create user: " + e.getMessage());
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
        } 
        
        catch (PasswordNotChangedException e) {
            throw new PasswordNotChangedException("Failed to change password: " + e.getMessage());

        } 
        
        catch (Exception e) {
            throw new InternalServerErrorException("An unexpected error occurred while changing the password");
        }
    }

    @Override
    @Transactional
    @ComplianceAudit(entityType = AuditEntityType.USER_DEACTIVATED, scope = "Verify activation status of User")
    public User updateUserStatus(UUID id, Status status, UserPrincipal principal) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    
            if (id.equals(principal.userId()) && status == Status.INACTIVE) {
                throw new AccessDeniedException("Admins cannot deactivate their own account.");
            }
    
    
            user.setStatus(status);
            return userRepository.save(user);
        } 
        
        catch(UserUpdateFailedException e) {
            log.error("Error occurred while updating user status for user {}: {}", id, e.getMessage());
            throw new UserUpdateFailedException("Failed to update user status: " + e.getMessage());

        }
        
        catch (Exception e) {
            log.error("Unexpected error occurred while updating user status for user {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while updating the user status");
        }
    }
}
