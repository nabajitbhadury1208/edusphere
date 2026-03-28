package com.cts.edusphere.repositories.user;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.modules.user.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link User} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom query methods for checking email uniqueness, resolving users by email,
 * and listing users by assigned role.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Checks whether a user with the given email address already exists in the database.
     *
     * <p>Used for duplicate-email validation during registration or profile updates.</p>
     *
     * @param email a valid email address (validated by {@link Email}) to check for existence
     * @return {@code true} if a user with the specified email exists; {@code false} otherwise
     */
    boolean existsByEmail(@Email String email);

    /**
     * Retrieves a user by their email address.
     *
     * <p>Typically used during authentication and security context resolution.</p>
     *
     * @param email the email address of the user to look up
     * @return an {@link Optional} containing the matching {@link User},
     *         or {@link Optional#empty()} if no user has the given email
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves all users who have been assigned a specific role.
     *
     * <p>Uses a collection-contains check on the {@code roles} field to find all users
     * whose role set includes the specified {@link Role}.</p>
     *
     * @param role the {@link Role} enum value (e.g., {@code ADMIN}, {@code FACULTY},
     *             {@code STUDENT}) to filter users by
     * @return a {@link List} of {@link User} instances that have the given role assigned;
     *         an empty list if no users hold that role
     */
    List<User> findAllByRolesContaining(Role role);
}
