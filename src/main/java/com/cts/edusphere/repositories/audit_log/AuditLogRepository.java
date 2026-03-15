package com.cts.edusphere.repositories.audit_log;

import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.modules.audit_log.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByUser_Id(UUID userId);

    List<AuditLog> findByResourceContainingIgnoreCase(String resource);

    List<AuditLog> findBySeverity(Severity severity);

    List<AuditLog> findByLogType(SystemLogType logType);

    List<AuditLog> findBySeverityAndLogType(Severity severity, SystemLogType logType);
}