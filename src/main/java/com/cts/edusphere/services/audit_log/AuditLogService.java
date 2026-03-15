package com.cts.edusphere.services.audit_log;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {

    void logSystemEvent(SystemLogType logType, Severity severity, String action, String resource, String details, UUID userId);
    List<AuditLogResponseDTO> getAllLogs();
    AuditLogResponseDTO getLogById(UUID id);
    List<AuditLogResponseDTO> getLogsByUser(UUID userId);
    List<AuditLogResponseDTO> getLogsByResource(String resource);
    List<AuditLogResponseDTO> getLogsBySeverity(Severity severity);
    List<AuditLogResponseDTO> getLogsByType(SystemLogType logType);
}