package com.cts.edusphere.services.auditLog;

import com.cts.edusphere.common.dto.audit_log.AuditLogResponseDTO;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.AuditLogMapper;
import com.cts.edusphere.modules.AuditLog;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuditLogMapper auditLogMapper;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    private UUID auditLogId;
    private UUID userId;
    private AuditLog auditLog;
    private User user;
    private AuditLogResponseDTO auditLogResponseDTO;

    @BeforeEach
    void setUp() {
        auditLogId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("John Doe");

        auditLog = new AuditLog();
        auditLog.setId(auditLogId);
        auditLog.setUser(user);
        auditLog.setAction("CREATE");
        auditLog.setResource("Faculty");
        auditLog.setTimestamp(Instant.now());
        auditLog.setCreatedAt(Instant.now());
        auditLog.setUpdatedAt(Instant.now());

        auditLogResponseDTO = new AuditLogResponseDTO(
                auditLogId,
                userId,
                "CREATE",
                "Faculty",
                null,
                Instant.now()
        );
    }

    @Test
    void testGetAllLogs_Success() {
        AuditLog auditLog2 = new AuditLog();
        auditLog2.setId(UUID.randomUUID());
        auditLog2.setAction("UPDATE");

        List<AuditLog> logs = List.of(auditLog, auditLog2);

        when(auditLogRepository.findAll()).thenReturn(logs);
        when(auditLogMapper.toResponseDTO(auditLog)).thenReturn(auditLogResponseDTO);
        when(auditLogMapper.toResponseDTO(auditLog2)).thenReturn(auditLogResponseDTO);

        List<AuditLogResponseDTO> result = auditLogService.getAllLogs();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(auditLogRepository, times(1)).findAll();
    }

    @Test
    void testGetAllLogs_Empty() {
        when(auditLogRepository.findAll()).thenReturn(List.of());

        List<AuditLogResponseDTO> result = auditLogService.getAllLogs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auditLogRepository, times(1)).findAll();
    }

    @Test
    void testGetLogById_Success() {
        when(auditLogRepository.findById(auditLogId)).thenReturn(Optional.of(auditLog));
        when(auditLogMapper.toResponseDTO(auditLog)).thenReturn(auditLogResponseDTO);

        AuditLogResponseDTO result = auditLogService.getLogById(auditLogId);

        assertNotNull(result);
        assertEquals(auditLogId, result.auditLogId());
        verify(auditLogRepository, times(1)).findById(auditLogId);
    }

    @Test
    void testGetLogById_NotFound() {
        when(auditLogRepository.findById(auditLogId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> auditLogService.getLogById(auditLogId));
        verify(auditLogRepository, times(1)).findById(auditLogId);
    }

    @Test
    void testGetLogsByUser_Success() {
        AuditLog auditLog2 = new AuditLog();
        auditLog2.setId(UUID.randomUUID());
        auditLog2.setUser(user);
        auditLog2.setAction("UPDATE");

        List<AuditLog> logs = List.of(auditLog, auditLog2);

        when(auditLogRepository.findByUser_Id(userId)).thenReturn(logs);
        when(auditLogMapper.toResponseDTO(auditLog)).thenReturn(auditLogResponseDTO);
        when(auditLogMapper.toResponseDTO(auditLog2)).thenReturn(auditLogResponseDTO);

        List<AuditLogResponseDTO> result = auditLogService.getLogsByUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(auditLogRepository, times(1)).findByUser_Id(userId);
    }

    @Test
    void testGetLogsByUser_Empty() {
        when(auditLogRepository.findByUser_Id(userId)).thenReturn(List.of());

        List<AuditLogResponseDTO> result = auditLogService.getLogsByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auditLogRepository, times(1)).findByUser_Id(userId);
    }

    @Test
    void testGetLogsByResource_Success() {
        AuditLog auditLog2 = new AuditLog();
        auditLog2.setId(UUID.randomUUID());
        auditLog2.setResource("Faculty");
        auditLog2.setAction("UPDATE");

        List<AuditLog> logs = List.of(auditLog, auditLog2);

        when(auditLogRepository.findByResourceContainingIgnoreCase("Faculty")).thenReturn(logs);
        when(auditLogMapper.toResponseDTO(auditLog)).thenReturn(auditLogResponseDTO);
        when(auditLogMapper.toResponseDTO(auditLog2)).thenReturn(auditLogResponseDTO);

        List<AuditLogResponseDTO> result = auditLogService.getLogsByResource("Faculty");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(auditLogRepository, times(1)).findByResourceContainingIgnoreCase("Faculty");
    }

    @Test
    void testGetLogsByResource_Empty() {
        when(auditLogRepository.findByResourceContainingIgnoreCase("NonExistent")).thenReturn(List.of());

        List<AuditLogResponseDTO> result = auditLogService.getLogsByResource("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auditLogRepository, times(1)).findByResourceContainingIgnoreCase("NonExistent");
    }

    @Test
    void testGetLogsByResource_CaseInsensitive() {
        List<AuditLog> logs = List.of(auditLog);

        when(auditLogRepository.findByResourceContainingIgnoreCase("faculty")).thenReturn(logs);
        when(auditLogMapper.toResponseDTO(auditLog)).thenReturn(auditLogResponseDTO);

        List<AuditLogResponseDTO> result = auditLogService.getLogsByResource("faculty");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(auditLogRepository, times(1)).findByResourceContainingIgnoreCase("faculty");
    }
}

