package com.cts.edusphere.services.user;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface defining the contract for user management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting user accounts,
 * as well as authentication-related operations such as registration, password changes,
 * and status management.</p>
 */
public interface UserService {

    /**
     * Retrieves all users registered in the system.
     *
     * @return a {@link List} of all {@link User} entities; never {@code null}, may be empty
     */
    public List<User> getAllUsers();

    /**
     * Retrieves a single user by their unique identifier.
     *
     * @param id the {@link UUID} of the user to retrieve
     * @return the {@link User} entity matching the given ID
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
     */
    public User getUserById(UUID id);

    /**
     * Persists a new user entity to the data store.
     *
     * @param user the {@link User} entity to create; must not be {@code null}
     * @return the saved {@link User} entity with any generated fields (e.g., ID) populated
     */
    public User createUser(User user);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address to search for; must not be {@code null}
     * @return the {@link User} entity whose email matches the given value
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given email
     */
    public User getUserByEmail(String email);

    /**
     * Deletes the user identified by the given ID.
     *
     * @param id the {@link UUID} of the user to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
     */
    public void deleteUserById(UUID id);

    /**
     * Updates the profile fields of an existing user.
     *
     * @param id            the {@link UUID} of the user to update
     * @param request       the {@link UserRequestDto} containing the updated field values
     * @param userPrincipal the currently authenticated {@link UserPrincipal} performing the update
     * @return the updated {@link User} entity
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
     */
    public User updateUserById(UUID id, UserRequestDto request, UserPrincipal userPrincipal);

    /**
     * Checks whether a user with the given email address already exists in the system.
     *
     * @param email the email address to check; must not be {@code null}
     * @return {@code true} if a user with the given email exists, {@code false} otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Attempts to find a user by email address, returning an empty {@link Optional} if not found.
     *
     * @param email the email address to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link User}, or empty if none found
     */
    Optional<User> findByEmail(String email);

    /**
     * Registers a new user from a public registration request, applying any default roles
     * and encoding the provided password before persisting.
     *
     * @param request the {@link RegisterRequest} containing registration details (name, email, password, etc.)
     * @return the newly created and persisted {@link User} entity
     * @throws com.cts.edusphere.exceptions.DuplicateResourceException if the email address is already in use
     */
    public User registerUser(RegisterRequest request);

    /**
     * Changes the password for the specified user after verifying the current password.
     *
     * @param userId          the {@link UUID} of the user whose password is to be changed
     * @param currentPassword the user's existing password, used for verification
     * @param newPassword     the new password to set; will be encoded before storage
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException  if no user exists with the given ID
     * @throws com.cts.edusphere.exceptions.BadRequestException        if {@code currentPassword} does not match the stored password
     */
    public void changePassword(UUID userId, String currentPassword, String newPassword);

    /**
     * Updates the account status (e.g., ACTIVE, INACTIVE, SUSPENDED) of the specified user.
     *
     * @param id        the {@link UUID} of the user whose status is to be updated
     * @param status    the new {@link Status} to apply to the user account
     * @param principal the currently authenticated {@link UserPrincipal} performing the status change
     * @return the updated {@link User} entity reflecting the new status
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no user exists with the given ID
     */
    public User updateUserStatus(UUID id, Status status, UserPrincipal principal);
}
