package com.cts.edusphere.services.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import java.util.List;
import java.util.UUID;

public interface AuditService {
    AuditResponseDTO createAudit(AuditRequestDTO dto);
    List<AuditResponseDTO> getAllAudits();
    AuditResponseDTO getAuditById(UUID id);
    AuditResponseDTO updateAudit(UUID id, AuditRequestDTO dto);
    void deleteAudit(UUID id);
}   