package com.cts.edusphere.services.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    List<AuditLogResponseDTO> getAllLogs();
    AuditLogResponseDTO getLogById(UUID id);
    List<AuditLogResponseDTO> getLogsByUser(UUID userId);
    List<AuditLogResponseDTO> getLogsByResource(String resource);
}