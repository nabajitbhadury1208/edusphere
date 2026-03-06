package com.cts.edusphere.services.compliancerecord;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ComplianceRecordMapper;
import com.cts.edusphere.modules.ComplianceOfficer;
import com.cts.edusphere.modules.ComplianceRecord;
import com.cts.edusphere.repositories.ComplianceOfficerRepository;
import com.cts.edusphere.repositories.ComplianceRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

    private final ComplianceRecordRepository recordRepository;
    private final ComplianceOfficerRepository officerRepository;
    private final ComplianceRecordMapper recordMapper;

    @Override
    public ComplianceRecordResponse createRecord(ComplianceRecordRequest request) {
        try {
            ComplianceOfficer officer = officerRepository.findById(request.recordedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Compliance Officer not found with ID: " + request.recordedByUserId()));

            ComplianceRecord record = recordMapper.toEntity(request, officer);
            return recordMapper.toResponse(recordRepository.save(record));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create compliance record: {}", e.getMessage());
            throw new InternalServerErrorException("An internal error occurred while saving the compliance record.");
        }
    }

    @Override
    public ComplianceRecordResponse updateRecord(UUID id, ComplianceRecordRequest request) {
        try {
            ComplianceRecord existing = recordRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Compliance record not found with ID: " + id));

            ComplianceOfficer officer = officerRepository.findById(request.recordedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Officer not found with ID: " + request.recordedByUserId()));

            // Updating fields based on entity structure
            existing.setRecordedBy(officer);
            existing.setEntityId(request.entityId());
            existing.setEntityType(request.entityType());
            existing.setComplianceType(request.complianceType());
            existing.setResult(request.result());
            existing.setComplianceDate(request.complianceDate());
            existing.setNotes(request.notes());

            return recordMapper.toResponse(recordRepository.save(existing));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update record {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Error updating compliance record.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceRecordResponse> getRecordsByEntityId(UUID entityId) {
        return recordRepository.findByEntityId(entityId).stream()
                .map(recordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceRecordResponse> getAllRecords() {
        return recordRepository.findAll().stream()
                .map(recordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ComplianceRecordResponse getRecordById(UUID id) {
        return recordRepository.findById(id)
                .map(recordMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance record not found with ID: " + id));
    }

    @Override
    public void deleteRecord(UUID id) {
        if (!recordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Compliance record not found");
        }
        recordRepository.deleteById(id);
        log.info("Deleted compliance record with ID: {}", id);
    }
}