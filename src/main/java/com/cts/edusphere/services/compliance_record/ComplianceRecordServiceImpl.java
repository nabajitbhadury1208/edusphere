package com.cts.edusphere.services.compliance_record;

import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordRequest;
import com.cts.edusphere.common.dto.compliance_record.ComplianceRecordResponse;
import com.cts.edusphere.exceptions.genericexceptions.ComplianceRecordNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.ComplianceRecordNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ComplianceRecordsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ComplianceRecordNotDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UpdatingComplianceRecordFailedException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;
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
                    .orElseThrow(() -> new ComplianceRecordNotFoundException("User not found with id: " + request.recordedByUserId()));
    
            ComplianceRecord record = complianceRecordMapper.toEntity(request);
            record.setComplianceOfficer(officer);
    
            return complianceRecordMapper.toResponseDto(complianceRecordRepository.save(record));
        } catch(ComplianceRecordNotCreatedException e) {
            throw new ComplianceRecordNotCreatedException(e.getMessage());
        } catch(Exception e) {
            log.error("Unexpected error occurred while creating compliance record: {}", e.getMessage());
            throw new ComplianceRecordNotCreatedException("Failed to create ComplianceRecord");
        }
    }

    @Override
    public List<ComplianceRecordResponse> getAllComplianceRecords() {
        try {
            return complianceRecordRepository.findAll().stream()
                    .map(complianceRecordMapper::toResponseDto)
                    .collect(Collectors.toList());
        } catch(ComplianceRecordsNotFoundException e) {
            throw new ComplianceRecordNotFoundException(e.getMessage());
        } catch(Exception e) {
            log.error("Unexpected error occurred while fetching compliance records: {}", e.getMessage());
            throw new ComplianceRecordsNotFoundException("Failed to retrieve ComplianceRecords");
        }
    }

    @Override
    public ComplianceRecordResponse getComplianceRecordById(UUID id) {
        try {
        ComplianceRecord record = complianceRecordRepository.findById(id)
                .orElseThrow(() -> new ComplianceRecordNotFoundException("ComplianceRecord not found with id: " + id));
        return complianceRecordMapper.toResponseDto(record);
    } catch(Exception e) {
            log.error("Unexpected error occurred while fetching compliance record: {}", e.getMessage());
            throw new ComplianceRecordNotFoundException("Failed to retrieve ComplianceRecord");
        }
    }

    @Override
    public List<ComplianceRecordResponse> getComplianceRecordsByEntityId(UUID entityId) {
        try {
            return complianceRecordRepository.findByEntityId(entityId).stream()
                    .map(complianceRecordMapper::toResponseDto)
                    .collect(Collectors.toList());
        } catch(ComplianceRecordNotFoundException e) {
            throw new ComplianceRecordNotFoundException(e.getMessage());
        } catch(Exception e) {
            log.error("Unexpected error occurred while fetching compliance records by entity ID: {}", e.getMessage());
            throw new ComplianceRecordNotFoundException("Failed to retrieve ComplianceRecords for entity ID: " + entityId);
        }
    }

    @Override
    public List<ComplianceRecordResponse> getComplianceRecordsByUserId(UUID userId) {
        try {
            return complianceRecordRepository.findByComplianceOfficer_Id(userId).stream()
                    .map(complianceRecordMapper::toResponseDto)
                    .collect(Collectors.toList());
        } catch(ComplianceRecordNotFoundException e) {
            throw new ComplianceRecordNotFoundException(e.getMessage());
        } catch(Exception e) {
            log.error("Unexpected error occurred while fetching compliance records by user ID: {}", e.getMessage());
            throw new ComplianceRecordNotFoundException("Failed to retrieve ComplianceRecords for user ID: " + userId);
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

        } catch(UpdatingComplianceRecordFailedException e) {
            throw new UpdatingComplianceRecordFailedException(e.getMessage());
        } catch(Exception e) {
            log.error("Unexpected error occurred while updating compliance record: {}", e.getMessage());
            throw new UpdatingComplianceRecordFailedException("Failed to update ComplianceRecord with id: " + id);
        }
    }

    @Override
    public void deleteComplianceRecordById(UUID id) {
        try {
            if (!complianceRecordRepository.existsById(id)) {
                throw new ComplianceRecordNotFoundException("ComplianceRecord not found with id: " + id);
            }
            complianceRecordRepository.deleteById(id);
            
        } catch (ComplianceRecordNotDeletedException e) {
            throw new ComplianceRecordNotDeletedException("Failed to delete ComplianceRecord with id: " + id);
        } catch (Exception e) {
            log.error("Error occurred while deleting compliance record with ID {}: {}", id, e.getMessage());
            throw new ComplianceRecordNotDeletedException("Failed to delete ComplianceRecord with id: " + id);
        }
    }
}