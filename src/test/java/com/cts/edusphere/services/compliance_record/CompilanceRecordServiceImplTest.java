package com.cts.edusphere.services.compliance_record;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.enums.ComplianceResult;
import com.cts.edusphere.enums.ComplianceType;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.compliance_record.ComplianceRecordMapper;
import com.cts.edusphere.modules.compliance_record.ComplianceRecord;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.compliance.ComplianceRecordRepository;
import com.cts.edusphere.repositories.user.UserRepository;
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

@ExtendWith(MockitoExtension.class)
class ComplianceRecordServiceImplTest {

    @Mock
    private ComplianceRecordRepository complianceRecordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ComplianceRecordMapper complianceRecordMapper;

    @InjectMocks
    private ComplianceRecordServiceImpl complianceRecordService;

    private User officer;
    private ComplianceRecord complianceRecord;
    private ComplianceRecordRequest request;
    private ComplianceRecordResponse response;
    private UUID recordId;
    private UUID userId;
    private UUID entityId;

    @BeforeEach
    void setUp() {
        recordId = UUID.randomUUID();
        userId = UUID.randomUUID();
        entityId = UUID.randomUUID();

        officer = User.builder()
                .id(userId)
                .name("Compliance Officer")
                .build();

        request = new ComplianceRecordRequest(
                userId, entityId, "COURSE", ComplianceType.COURSE,
                ComplianceResult.PASS, LocalDate.now(), "Notes"
        );

        complianceRecord = ComplianceRecord.builder()
                .id(recordId)
                .entityId(entityId)
                .entityType("COURSE")
                .complianceType(ComplianceType.COURSE)
                .result(ComplianceResult.PASS)
                .complianceOfficer(officer)
                .complianceDate(LocalDate.now())
                .notes("Notes")
                .build();

        response = new ComplianceRecordResponse(
                recordId, userId, entityId, "COURSE",
                ComplianceType.COURSE, ComplianceResult.PASS,
                LocalDate.now(), "Notes", Instant.now()
        );
    }

    @Test
    void createComplianceRecord_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(officer));
        when(complianceRecordMapper.toEntity(request)).thenReturn(complianceRecord);
        when(complianceRecordRepository.save(any(ComplianceRecord.class))).thenReturn(complianceRecord);
        when(complianceRecordMapper.toResponseDto(complianceRecord)).thenReturn(response);

        ComplianceRecordResponse result = complianceRecordService.createComplianceRecord(request);

        assertNotNull(result);
        assertEquals(userId, result.recordedByUserId());
        verify(complianceRecordRepository).save(any(ComplianceRecord.class));
    }

    @Test
    void getComplianceRecordById_ThrowsException_WhenNotFound() {
        when(complianceRecordRepository.findById(recordId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                complianceRecordService.getComplianceRecordById(recordId));
    }

    @Test
    void getComplianceRecordsByEntityId_ReturnsList() {
        when(complianceRecordRepository.findByEntityId(entityId)).thenReturn(List.of(complianceRecord));
        when(complianceRecordMapper.toResponseDto(complianceRecord)).thenReturn(response);

        List<ComplianceRecordResponse> results = complianceRecordService.getComplianceRecordsByEntityId(entityId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void updateComplianceRecord_UpdatesExistingEntity() {
        when(complianceRecordRepository.findById(recordId)).thenReturn(Optional.of(complianceRecord));
        when(userRepository.findById(userId)).thenReturn(Optional.of(officer));

        complianceRecordService.updateComplianceRecord(recordId, request);

        verify(complianceRecordRepository).save(complianceRecord);
        assertEquals(entityId, complianceRecord.getEntityId());
    }

    @Test
    void deleteComplianceRecord_Success() {
        when(complianceRecordRepository.existsById(recordId)).thenReturn(true);

        complianceRecordService.deleteComplianceRecordById(recordId);

        verify(complianceRecordRepository).deleteById(recordId);
    }
}