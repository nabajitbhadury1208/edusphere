package com.cts.edusphere.services.audit;

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.enums.AuditStatus;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.AuditMapper;
import com.cts.edusphere.modules.Audit;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.AuditRepository;
import com.cts.edusphere.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditMapper auditMapper;

    @InjectMocks
    private AuditServiceImpl auditService;

    private UUID auditId;
    private UUID officerId;
    private Audit audit;
    private User officer;
    private AuditRequestDTO auditRequestDTO;
    private AuditResponseDTO auditResponseDTO;

    @BeforeEach
    void setUp() {
        auditId = UUID.randomUUID();
        officerId = UUID.randomUUID();

        officer = new User();
        officer.setId(officerId);
        officer.setName("Compliance Officer");
        officer.setRoles(java.util.Set.of(Role.COMPLIANCE_OFFICER));
        officer.setStatus(Status.ACTIVE);

        audit = new Audit();
        audit.setId(auditId);
        audit.setScope("Academic Audit");
        audit.setFindings("All systems compliant");
        audit.setAuditDate(LocalDate.now());
        audit.setStatus(AuditStatus.PENDING);
        audit.setComplianceOfficer(officer);
        audit.setCreatedAt(Instant.now());
        audit.setUpdatedAt(Instant.now());

        auditRequestDTO = new AuditRequestDTO(
                officerId,
                "Academic Audit",
                "All systems compliant",
                LocalDate.now().toString()
        );

        auditResponseDTO = new AuditResponseDTO(
                auditId,
                officerId,
                "Academic Audit",
                "All systems compliant",
                LocalDate.now(),
                AuditStatus.PENDING
        );
    }

    @Test
    void testCreateAudit_Success() {
        when(userRepository.findById(officerId)).thenReturn(Optional.of(officer));
        when(auditMapper.toEntity(auditRequestDTO)).thenReturn(audit);
        when(auditRepository.save(any(Audit.class))).thenReturn(audit);
        when(auditMapper.toResponseDTO(audit)).thenReturn(auditResponseDTO);

        AuditResponseDTO result = auditService.createAudit(auditRequestDTO);

        assertNotNull(result);
        assertEquals(auditId, result.auditId());
        verify(userRepository, times(1)).findById(officerId);
        verify(auditRepository, times(1)).save(any(Audit.class));
    }

    @Test
    void testCreateAudit_OfficerNotFound() {
        when(userRepository.findById(officerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> auditService.createAudit(auditRequestDTO));
        verify(userRepository, times(1)).findById(officerId);
        verify(auditRepository, never()).save(any());
    }

    @Test
    void testGetAllAudits_Success() {
        Audit audit2 = new Audit();
        audit2.setId(UUID.randomUUID());
        audit2.setScope("Financial Audit");

        List<Audit> audits = List.of(audit, audit2);

        when(auditRepository.findAll()).thenReturn(audits);
        when(auditMapper.toResponseDTO(audit)).thenReturn(auditResponseDTO);
        when(auditMapper.toResponseDTO(audit2)).thenReturn(auditResponseDTO);

        List<AuditResponseDTO> result = auditService.getAllAudits();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(auditRepository, times(1)).findAll();
    }

    @Test
    void testGetAllAudits_Empty() {
        when(auditRepository.findAll()).thenReturn(List.of());

        List<AuditResponseDTO> result = auditService.getAllAudits();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auditRepository, times(1)).findAll();
    }

    @Test
    void testGetAuditById_Success() {
        when(auditRepository.findById(auditId)).thenReturn(Optional.of(audit));
        when(auditMapper.toResponseDTO(audit)).thenReturn(auditResponseDTO);

        AuditResponseDTO result = auditService.getAuditById(auditId);

        assertNotNull(result);
        assertEquals(auditId, result.auditId());
        verify(auditRepository, times(1)).findById(auditId);
    }

    @Test
    void testGetAuditById_NotFound() {
        when(auditRepository.findById(auditId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> auditService.getAuditById(auditId));
        verify(auditRepository, times(1)).findById(auditId);
    }

    @Test
    void testUpdateAudit_Success() {
        when(auditRepository.findById(auditId)).thenReturn(Optional.of(audit));
        when(auditRepository.save(any(Audit.class))).thenReturn(audit);
        when(auditMapper.toResponseDTO(audit)).thenReturn(auditResponseDTO);

        AuditResponseDTO result = auditService.updateAudit(auditId, auditRequestDTO);

        assertNotNull(result);
        verify(auditRepository, times(1)).findById(auditId);
        verify(auditRepository, times(1)).save(any(Audit.class));
    }

    @Test
    void testUpdateAudit_NotFound() {
        when(auditRepository.findById(auditId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> auditService.updateAudit(auditId, auditRequestDTO));
        verify(auditRepository, never()).save(any());
    }


    @Test
    void testDeleteAudit_Success() {
        when(auditRepository.existsById(auditId)).thenReturn(true);
        doNothing().when(auditRepository).deleteById(auditId);

        auditService.deleteAudit(auditId);

        verify(auditRepository, times(1)).existsById(auditId);
        verify(auditRepository, times(1)).deleteById(auditId);
    }

    @Test
    void testDeleteAudit_NotFound() {
        when(auditRepository.existsById(auditId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> auditService.deleteAudit(auditId));
        verify(auditRepository, never()).deleteById(any());
    }
}

