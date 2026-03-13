package com.cts.edusphere.services.audit_log;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    String createLog(AuditLogRequestDTO auditLogRequestDTO);
    List<AuditLogResponseDTO> getAllLogs();
    AuditLogResponseDTO getLogById(UUID id);
    List<AuditLogResponseDTO> getLogsByUser(UUID userId);
    List<AuditLogResponseDTO> getLogsByResource(String resource);
}