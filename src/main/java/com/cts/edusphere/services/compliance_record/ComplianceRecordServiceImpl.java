package com.cts.edusphere.services.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.exceptions.genericexceptions.*;
import com.cts.edusphere.mappers.compliance_record.ComplianceRecordMapper;
import com.cts.edusphere.modules.compliance_record.ComplianceRecord;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.compliance.ComplianceRecordRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

    private final ComplianceRecordRepository complianceRecordRepository;
    private final UserRepository userRepository;
    private final ComplianceRecordMapper complianceRecordMapper;

    @Override
    public ComplianceRecordResponse createComplianceRecord(ComplianceRecordRequest request) {

        try {

            User officer = userRepository.findById(request.recordedByUserId())
                    .orElseThrow(() -> new UserNotFoundException("Compliance Officer not found with id: " + request.recordedByUserId()));

            ComplianceRecord record = complianceRecordMapper.toEntity(request);
            record.setComplianceOfficer(officer);

            ComplianceRecord savedRecord = complianceRecordRepository.save(record);
            log.info("Compliance record created successfully with ID: {}", savedRecord.getId());
            return complianceRecordMapper.toResponseDto(savedRecord);

        } catch (UserNotFoundException e) {
            log.error("Officer validation failed: {}", e.getMessage());
            throw e;
        } catch (ComplianceRecordNotCreatedException e) {
            log.error("Domain failure during compliance creation: {}", e.getMessage());
            throw new ComplianceRecordNotCreatedException("Failed to create compliance record: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating compliance record: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred during creation");
        }
    }

    @Override
    public List<ComplianceRecordResponse> getAllComplianceRecords() {
        try {
            List<ComplianceRecord> records = complianceRecordRepository.findAll();
            if (records.isEmpty()) {
                throw new ComplianceRecordsNotFoundException("No compliance records found in the system.");
            }
            return records.stream()
                    .map(complianceRecordMapper::toResponseDto)
                    .collect(Collectors.toList());
        } catch (ComplianceRecordsNotFoundException e) {
            log.error("No records found: {}", e.getMessage());
            throw e;
        } catch(Exception e) {
            log.error("Unexpected error occurred while fetching compliance records: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve compliance records list");
        }
    }

    @Override
    public ComplianceRecordResponse getComplianceRecordById(UUID id) {
        try {
            return complianceRecordRepository.findById(id)
                    .map(complianceRecordMapper::toResponseDto)
                    .orElseThrow(() -> new ComplianceRecordNotFoundException("Compliance record not found with id: " + id));
        } catch (ComplianceRecordNotFoundException e) {
            log.error("Record {} not found: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching record {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve compliance record details");
        }
    }

    @Override
    public List<ComplianceRecordResponse> getComplianceRecordsByEntityId(UUID entityId) {
        try {
            List<ComplianceRecord> records = complianceRecordRepository.findByEntityId(entityId);

            if (records.isEmpty()) {
                throw new ComplianceRecordNotFoundException("No compliance records found for entity ID: " + entityId);
            }

            return records.stream()
                    .map(complianceRecordMapper::toResponseDto)
                    .collect(Collectors.toList());

        } catch (ComplianceRecordNotFoundException e) {
            log.error("Compliance lookup failed for entity {}: {}", entityId, e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error fetching compliance records by entity ID {}: {}", entityId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve compliance records for the specified entity");
        }
    }

    @Override
    public List<ComplianceRecordResponse> getComplianceRecordsByUserId(UUID userId) {
        try {
            List<ComplianceRecord> records = complianceRecordRepository.findByComplianceOfficer_Id(userId);

            if (records.isEmpty()) {
                throw new ComplianceRecordNotFoundException("No compliance records found for officer ID: " + userId);
            }

            return records.stream()
                    .map(complianceRecordMapper::toResponseDto)
                    .collect(Collectors.toList());

        } catch (ComplianceRecordNotFoundException e) {
            log.error("Compliance lookup failed for officer {}: {}", userId, e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error fetching compliance records by officer ID {}: {}", userId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve compliance records for the specified officer");
        }
    }

    @Override
    public void updateComplianceRecord(UUID id, ComplianceRecordRequest request) {
        try {
            ComplianceRecord record = complianceRecordRepository.findById(id)
                    .orElseThrow(() -> new ComplianceRecordNotFoundException("ComplianceRecord not found with id: " + id));

            if (request.recordedByUserId() != null) {
                User officer = userRepository.findById(request.recordedByUserId())
                        .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.recordedByUserId()));
                record.setComplianceOfficer(officer);
            }

            if (request.entityId() != null) record.setEntityId(request.entityId());
            if (request.entityType() != null) record.setEntityType(request.entityType());
            if (request.complianceType() != null) record.setComplianceType(request.complianceType());
            if (request.result() != null) record.setResult(request.result());
            if (request.complianceDate() != null) record.setComplianceDate(request.complianceDate());
            if (request.notes() != null) record.setNotes(request.notes());

            complianceRecordRepository.save(record);

        } catch (ComplianceRecordNotFoundException | UserNotFoundException e) {
            log.error("Update validation failed: {}", e.getMessage());
            throw e;
        } catch (UpdatingComplianceRecordFailedException e) {
            log.error("Domain failure during update of record {}: {}", id, e.getMessage());
            throw new UpdatingComplianceRecordFailedException("Failed to update record: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error updating record {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update compliance record");
        }
    }

    @Override
    public void deleteComplianceRecordById(UUID id) {
        try {
            if (!complianceRecordRepository.existsById(id)) {
                throw new ComplianceRecordNotFoundException("Compliance record not found with id: " + id);
            }
            complianceRecordRepository.deleteById(id);
            log.info("Compliance record deleted successfully: {}", id);

        } catch (ComplianceRecordNotFoundException e) {
            log.error("Deletion failed - record {} not found", id);
            throw e;
        } catch (ComplianceRecordNotDeletedException e) {
            log.error("Domain failure during deletion of record {}: {}", id, e.getMessage());
            throw new ComplianceRecordNotDeletedException("Failed to delete record: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error deleting record {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete compliance record");
        }
    }

    }
