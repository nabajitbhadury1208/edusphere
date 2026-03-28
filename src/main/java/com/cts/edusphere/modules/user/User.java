package com.cts.edusphere.modules.user;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * JPA entity representing a system user in the EduSphere application.
 *
 * <p>{@code User} is the root of the inheritance hierarchy for all human actors in
 * the system. Concrete sub-types (e.g., {@code Student}, {@code Faculty}) extend this
 * class via a JOINED inheritance strategy, storing their additional columns in their
 * own tables while sharing the common user fields held here in the {@code users}
 * table.</p>
 *
 * <p>Each user has a unique email address, an optional unique phone number, a set of
 * {@link Role}s that determine their permissions, a hashed password (write-only in JSON
 * serialization), and a lifecycle {@link Status}.</p>
 *
 * <p>This class extends {@link BaseEntity}, inheriting the auto-generated UUID primary
 * key (mapped to {@code user_id}), optimistic-locking version, and Spring Data JPA
 * auditing timestamps.</p>
 *
 * @see Role
 * @see Status
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "user_id"))

public class User extends BaseEntity {

    /**
     * The full display name of the user.
     * Must not be blank; validated by Bean Validation and enforced as non-null
     * at the database level.
     */
    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false, name = "name")
    private String name;

    /**
     * The set of roles assigned to this user (e.g., ADMIN, FACULTY, STUDENT).
     * Stored in the {@code user_roles} collection table, joined on {@code user_id}.
     * Eagerly fetched to make roles available immediately after loading the user.
     * Each role value is stored as a string.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Set<Role> roles;

    /**
     * The user's email address. Must be a valid email format (validated by
     * {@link Email}) and must be unique across all users. Non-nullable.
     */
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The user's contact phone number. Must match the pattern
     * {@code ^\+?[0-9]{7,15}$} (7–15 digits with an optional leading '+').
     * Unique across all users; may be {@code null} if not provided.
     */
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be 7 - 15 digits")
    @Column(unique = true)
    private String phone;

    /**
     * The current account status of this user (e.g., ACTIVE, INACTIVE, SUSPENDED).
     * Stored as a string; must not be {@code null}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * The user's hashed password. Non-nullable; must not be blank (validated by
     * {@link NotBlank}). Marked as write-only in JSON serialization so that it is
     * never included in API responses.
     */
    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, name = "password")
    private String password;
}
