package com.cts.edusphere.core;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base entity class that provides common auditing and identity fields
 * for all JPA entities in the EduSphere application.
 *
 * <p>All persistent domain objects should extend this class to inherit
 * auto-generated UUID primary keys, optimistic-locking via a version counter,
 * and Spring Data JPA auditing fields ({@code createdAt}, {@code updatedAt},
 * {@code createdBy}).</p>
 *
 * <p>The class is annotated with {@link MappedSuperclass} so that JPA maps its
 * fields to the owning entity's table rather than creating a separate table for
 * this class.</p>
 *
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
public class BaseEntity {

    /**
     * Auto-generated UUID primary key for the entity.
     * The value is produced by Hibernate's {@link UuidGenerator} and is
     * mapped to a column named {@code id} (overridden by subclasses as needed).
     */
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    /**
     * Timestamp recording when the entity was first persisted.
     * Populated automatically by Spring Data JPA auditing; never updated
     * after the initial insert ({@code updatable = false}).
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Timestamp recording the last time the entity was modified.
     * Updated automatically by Spring Data JPA auditing on every merge.
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * UUID of the user who originally created this entity.
     * Populated automatically by Spring Data JPA auditing; never updated
     * after the initial insert ({@code updatable = false}).
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    /**
     * Optimistic-locking version counter managed by JPA.
     * Incremented automatically on every update; a stale-state exception is
     * thrown if two concurrent transactions attempt to modify the same row.
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * Compares this entity with another object for equality based solely on
     * the entity's {@link #id} value.
     *
     * <p>Two instances are considered equal when they are the same object
     * reference, or when both have a non-{@code null} {@code id} that compares
     * equal. Transient instances (with a {@code null} id) are never equal to
     * any other instance.</p>
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    /**
     * Returns a hash code based on the runtime class of this entity rather
     * than on field values, which keeps the contract with {@link #equals}
     * consistent across the JPA lifecycle (transient vs. managed state).
     *
     * @return a stable hash code for this entity type
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
