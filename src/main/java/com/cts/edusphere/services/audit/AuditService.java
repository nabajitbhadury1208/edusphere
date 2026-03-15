package com.cts.edusphere.services.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.enums.AuditEntityType;

import java.util.List;
import java.util.UUID;

public interface AuditService {
    AuditResponseDTO reviewAudit(UUID auditId, AuditRequestDTO dto);
    List<AuditResponseDTO> getAllAudits();
    AuditResponseDTO getAuditById(UUID id);
    void deleteAudit(UUID id);
    List<AuditResponseDTO> getAuditsByEntityType(AuditEntityType entityType);
}   