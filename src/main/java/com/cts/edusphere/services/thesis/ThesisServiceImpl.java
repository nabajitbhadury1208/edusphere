package com.cts.edusphere.services.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ThesisMapper;
import com.cts.edusphere.modules.Thesis;
import com.cts.edusphere.repositories.ThesisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ThesisServiceImpl implements ThesisService {

    private final ThesisRepository thesisRepository;
    private final ThesisMapper thesisMapper;

    @Override
    public ThesisResponseDto createThesis(ThesisRequestDto request) {
        try {
            Thesis thesis = thesisMapper.toEntity(request);
            Thesis savedThesis = thesisRepository.save(thesis);
            log.info("Thesis created successfully with ID: {}", savedThesis.getId());
            return thesisMapper.toResponse(savedThesis);
        } catch (Exception e) {
            log.error("Error occurred while creating thesis: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create thesis record");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ThesisResponseDto getThesisById(UUID id) {
        try {
            return thesisRepository.findById(id)
                    .map(thesisMapper::toResponse)
                    .orElseThrow(() -> new ResourceNotFoundException("Thesis not found with id: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while fetching thesis {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve thesis details");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThesisResponseDto> getThesisByStudent(UUID studentId) {
        try {
            return thesisRepository.findByStudentId(studentId).stream()
                    .map(thesisMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching thesis for student {}: {}", studentId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve student thesis");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThesisResponseDto> getThesisBySupervisor(UUID facultyId) {
        try {
            return thesisRepository.findBySupervisorId(facultyId).stream()
                    .map(thesisMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching thesis for supervisor {}: {}", facultyId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve supervisor thesis");
        }
    }

    @Override
    public ThesisResponseDto updateThesis(UUID id, ThesisRequestDto request) {
        try {
            Thesis existing = thesisRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Thesis not found with id: " + id));

            // Implement specific field updates here based on Thesis Entity
            // if(request.title() != null) existing.setTitle(request.title());

            Thesis updatedThesis = thesisRepository.save(existing);
            log.info("Thesis record updated successfully: {}", id);
            return thesisMapper.toResponse(updatedThesis);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating thesis {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update thesis record");
        }
    }

    @Override
    public void deleteThesis(UUID id) {
        try {
            if (!thesisRepository.existsById(id)) {
                throw new ResourceNotFoundException("Thesis not found with id: " + id);
            }
            thesisRepository.deleteById(id);
            log.info("Thesis record deleted successfully: {}", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while deleting thesis {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete thesis record");
        }
    }
}