package com.cts.edusphere.repositories.audit;

import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.modules.audit.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<Audit, UUID> {
    List<Audit> findByEntityType(AuditEntityType entityType);
}

