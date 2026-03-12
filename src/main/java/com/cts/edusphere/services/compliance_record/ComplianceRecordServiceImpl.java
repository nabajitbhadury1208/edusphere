package com.cts.edusphere.services.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ComplianceRecordMapper;
import com.cts.edusphere.modules.ComplianceRecord;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.ComplianceRecordRepository;
import com.cts.edusphere.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

    private final ComplianceRecordRepository complianceRecordRepository;
    private final UserRepository userRepository;
    private final ComplianceRecordMapper complianceRecordMapper;

    @Override
    public ComplianceRecordResponse createComplianceRecord(ComplianceRecordRequest request) {
        User officer = userRepository.findById(request.recordedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.recordedByUserId()));

        ComplianceRecord record = complianceRecordMapper.toEntity(request);
        record.setComplianceOfficer(officer);

        return complianceRecordMapper.toResponseDto(complianceRecordRepository.save(record));
    }

    @Override
    public List<ComplianceRecordResponse> getAllComplianceRecords() {
        return complianceRecordRepository.findAll().stream()
                .map(complianceRecordMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ComplianceRecordResponse getComplianceRecordById(UUID id) {
        ComplianceRecord record = complianceRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComplianceRecord not found with id: " + id));
        return complianceRecordMapper.toResponseDto(record);
    }

    @Override
    public List<ComplianceRecordResponse> getComplianceRecordsByEntityId(UUID entityId) {
        return complianceRecordRepository.findByEntityId(entityId).stream()
                .map(complianceRecordMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplianceRecordResponse> getComplianceRecordsByUserId(UUID userId) {
        return complianceRecordRepository.findByComplianceOfficer_Id(userId).stream()
                .map(complianceRecordMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateComplianceRecord(UUID id, ComplianceRecordRequest request) {
        ComplianceRecord record = complianceRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComplianceRecord not found with id: " + id));

        if (request.recordedByUserId() != null) {
            User officer = userRepository.findById(request.recordedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.recordedByUserId()));
            record.setComplianceOfficer(officer);
        }

        if (request.entityId() != null) record.setEntityId(request.entityId());
        if (request.entityType() != null) record.setEntityType(request.entityType());
        if (request.complianceType() != null) record.setComplianceType(request.complianceType());
        if (request.result() != null) record.setResult(request.result());
        if (request.complianceDate() != null) record.setComplianceDate(request.complianceDate());
        if (request.notes() != null) record.setNotes(request.notes());

        complianceRecordRepository.save(record);
    }

    @Override
    public void deleteComplianceRecordById(UUID id) {
        if (!complianceRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("ComplianceRecord not found with id: " + id);
        }
        complianceRecordRepository.deleteById(id);
    }
}