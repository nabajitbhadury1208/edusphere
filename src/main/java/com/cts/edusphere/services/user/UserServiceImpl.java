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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.*;

/**
 * Implementation of {@link UserService} that provides user management operations
 * such as creation, retrieval, update, deletion, registration, authentication
 * support, and status management.
 *
 * <p>All database interactions are delegated to {@link com.cts.edusphere.repositories.user.UserRepository}.
 * Password encoding is handled via Spring Security's {@link org.springframework.security.crypto.password.PasswordEncoder}.
 * Audit log entries are nullified before user deletion to preserve referential integrity.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogRepository auditLogRepository;

    /**
     * Retrieves all users stored in the system.
     *
     * @return a {@link List} of all {@link User} entities; never {@code null}
     * @throws UsersNotFoundException      if no users are found during the fetch
     * @throws InternalServerErrorException if an unexpected error occurs while retrieving user records
     */
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

    /**
     * Retrieves a single user by their unique identifier.
     *
     * @param id the {@link UUID} of the user to retrieve
     * @return the {@link User} entity matching the given {@code id}
     * @throws UserNotFoundException        if no user exists with the specified {@code id}
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
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
    
    /**
     * Persists a new {@link User} entity to the database.
     *
     * @param user the {@link User} entity to create; must not be {@code null}
     * @return the saved {@link User} entity with any auto-generated fields populated
     * @throws UserCreationFailedException  if the user record cannot be created
     * @throws InternalServerErrorException if an unexpected error occurs during creation
     */
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

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address to search for; must not be {@code null}
     * @return the {@link User} entity associated with the given {@code email}
     * @throws UserNotFoundException        if no user is found with the specified {@code email}
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
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

    /**
     * Deletes a user by their unique identifier.
     *
     * <p>Before deletion, all audit log entries referencing this user are nullified
     * to preserve referential integrity in the audit log table.</p>
     *
     * @param id the {@link UUID} of the user to delete
     * @throws UserNotFoundException        if no user exists with the specified {@code id}
     * @throws UserDeletionFailedException  if the deletion operation fails
     * @throws InternalServerErrorException if an unexpected error occurs during deletion
     */
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

    /**
     * Updates an existing user's details identified by their unique identifier.
     *
     * <p>Regular users may only update their own {@code name} and {@code phone}.
     * Only administrators may modify a user's {@code roles} or {@code status}.
     * Attempting to update another user's record without admin privileges throws
     * {@link InsufficientPermissionException}.</p>
     *
     * @param id            the {@link UUID} of the user to update
     * @param request       a {@link UserRequestDto} containing the fields to update
     * @param userPrincipal the currently authenticated principal, used for permission checks
     * @return the updated {@link User} entity
     * @throws ResourceNotFoundException    if no user exists with the specified {@code id}
     * @throws InsufficientPermissionException if the caller lacks the required privileges
     * @throws UserUpdateFailedException    if the update operation fails
     * @throws InternalServerErrorException if an unexpected error occurs during the update
     */
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

    /**
     * Checks whether a user with the given email address already exists in the system.
     *
     * @param email the email address to check; must not be {@code null}
     * @return {@code true} if a user with the specified {@code email} exists, {@code false} otherwise
     * @throws EmailAlreadyExistsException  if an error occurs specifically related to email uniqueness
     * @throws InternalServerErrorException if an unexpected error occurs during the existence check
     */
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

    /**
     * Finds a user by their email address, returning an {@link Optional} result.
     *
     * <p>Unlike {@link #getUserByEmail(String)}, this method does not throw a
     * {@link UserNotFoundException} when the user is absent; instead it returns
     * an empty {@link Optional}.</p>
     *
     * @param email the email address to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link User}, or empty if not found
     * @throws UserNotFoundException        if a repository-level error referencing the user occurs
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
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

    /**
     * Registers a new user in the system using the provided registration details.
     *
     * <p>The supplied password is encoded before persistence. The new account is
     * automatically assigned an {@link com.cts.edusphere.enums.Status#ACTIVE} status.
     * Registration fails immediately if the requested email address is already in use.</p>
     *
     * @param request a {@link RegisterRequest} containing the new user's details
     *                (name, email, phone, password, roles); must not be {@code null}
     * @return the newly created {@link User} entity with all auto-generated fields populated
     * @throws EmailAlreadyExistsException  if the email address is already registered
     * @throws UserCreationFailedException  if the user record cannot be persisted
     */
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

    /**
     * Changes the password for the specified user after validating the current password.
     *
     * <p>The current password is verified against the stored encoded value before
     * the new password is encoded and saved.</p>
     *
     * @param userId          the {@link UUID} of the user whose password is to be changed
     * @param currentPassword the user's existing plaintext password for verification
     * @param newPassword     the new plaintext password to set; will be encoded before storage
     * @throws InvalidPasswordException     if {@code currentPassword} does not match the stored password
     * @throws PasswordNotChangedException  if the password update operation fails
     * @throws InternalServerErrorException if an unexpected error occurs during the operation
     */
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

    /**
     * Updates the {@link Status} of a user identified by the given {@code id}.
     *
     * <p>An administrator is not permitted to deactivate their own account; attempting
     * to do so with a {@code status} of {@link Status#INACTIVE} while the {@code principal}
     * matches the target {@code id} will throw an {@link java.nio.file.AccessDeniedException}.
     * This operation is audited via the {@link com.cts.edusphere.aspects.ComplianceAudit} aspect.</p>
     *
     * @param id        the {@link UUID} of the user whose status is to be updated
     * @param status    the new {@link Status} to assign to the user
     * @param principal the currently authenticated principal, used to prevent self-deactivation
     * @return the updated {@link User} entity
     * @throws UserNotFoundException        if no user exists with the specified {@code id}
     * @throws java.nio.file.AccessDeniedException if an admin attempts to deactivate their own account
     * @throws UserUpdateFailedException    if the status update operation fails
     * @throws InternalServerErrorException if an unexpected error occurs during the update
     */
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
