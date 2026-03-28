package com.cts.edusphere.repositories.audit;

import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.modules.audit.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Audit} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * a custom query method for retrieving audit records filtered by the type of
 * entity that was audited.</p>
 */
@Repository
public interface AuditRepository extends JpaRepository<Audit, UUID> {

    /**
     * Retrieves all audit records associated with a specific entity type.
     *
     * @param entityType the {@link AuditEntityType} enum value representing the category
     *                   of entity that was audited (e.g., {@code STUDENT}, {@code FACULTY},
     *                   {@code COURSE})
     * @return a {@link List} of {@link Audit} instances matching the given entity type;
     *         an empty list if no audit records are found for that entity type
     */
    List<Audit> findByEntityType(AuditEntityType entityType);
}
