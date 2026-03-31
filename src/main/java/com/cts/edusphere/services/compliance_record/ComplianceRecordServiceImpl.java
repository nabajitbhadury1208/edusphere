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

/**
 * Service implementation for managing compliance records.
 * Handles CRUD operations and filtering by entity ID or officer user ID.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

    private final ComplianceRecordRepository complianceRecordRepository;
    private final UserRepository userRepository;
    private final ComplianceRecordMapper complianceRecordMapper;

    /**
     * Creates a new compliance record and associates it with a compliance officer.
     * Resolves the officer User entity from the repository before persisting.
     *
     * @param request the request containing officer ID, entity ID/type, compliance type, result, and notes
     * @return the created ComplianceRecordResponse
     * @throws ComplianceRecordNotFoundException if the officer user is not found
     * @throws ComplianceRecordNotCreatedException if creation fails
     */
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

    /**
     * Retrieves all compliance records from the database.
     *
     * @return a list of all ComplianceRecordResponse objects
     * @throws ComplianceRecordsNotFoundException if retrieval fails
     */
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

    /**
     * Retrieves a specific compliance record by its unique identifier.
     *
     * @param id the UUID of the compliance record to retrieve
     * @return the matching ComplianceRecordResponse
     * @throws ComplianceRecordNotFoundException if no record with the given ID exists
     */
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

    /**
     * Retrieves all compliance records associated with a specific entity.
     *
     * @param entityId the UUID of the entity to filter by
     * @return a list of ComplianceRecordResponse objects for the given entity
     * @throws ComplianceRecordNotFoundException if retrieval fails
     */
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

    /**
     * Retrieves all compliance records created by a specific compliance officer.
     *
     * @param userId the UUID of the compliance officer user
     * @return a list of ComplianceRecordResponse objects for the given officer
     * @throws ComplianceRecordNotFoundException if retrieval fails
     */
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

    /**
     * Updates an existing compliance record with the provided fields (partial update).
     * Only non-null fields in the request are applied to the existing record.
     *
     * @param id      the UUID of the compliance record to update
     * @param request the request DTO containing updated fields
     * @throws ComplianceRecordNotFoundException     if no record with the given ID exists
     * @throws UpdatingComplianceRecordFailedException if the update fails
     */
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

    /**
     * Permanently deletes a compliance record by its unique identifier.
     * Verifies existence before deletion.
     *
     * @param id the UUID of the compliance record to delete
     * @throws ComplianceRecordNotFoundException     if no record with the given ID exists
     * @throws ComplianceRecordNotDeletedException   if deletion fails
     */
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
