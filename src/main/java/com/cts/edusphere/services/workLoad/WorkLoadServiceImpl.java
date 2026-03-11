package com.cts.edusphere.services.workLoad;

import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.WorkLoadMapper;
import com.cts.edusphere.modules.WorkLoad;
import com.cts.edusphere.repositories.CourseRepository;
import com.cts.edusphere.repositories.UserRepository;
import com.cts.edusphere.repositories.WorkLoadRepository;
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
public class WorkLoadServiceImpl implements WorkLoadService {

    private final WorkLoadRepository repository;
    private final WorkLoadMapper mapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public WorkLoadResponseDto createWorkLoad(WorkLoadRequestDto request) {
        try {
            WorkLoad workLoad = mapper.toEntity(request);
            if (request.courseId() != null) {
                workLoad.setCourse(courseRepository.getReferenceById(request.courseId()));
            }
            if (request.facultyId() != null) {
                workLoad.setFaculty(userRepository.getReferenceById(request.facultyId()));
            }
            WorkLoad savedWorkLoad = repository.save(workLoad);

            log.info("Workload record created successfully with ID: {}", savedWorkLoad.getId());
            return mapper.toResponse(savedWorkLoad);
        } catch (Exception e) {
            log.error("Error occurred while creating workload: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create workload record: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkLoadResponseDto> getAllWorkLoads() {
        try {
            return repository.findAll().stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching all workloads: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve workloads list");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WorkLoadResponseDto getWorkLoadById(UUID id) {
        try {
            return repository.findById(id)
                    .map(mapper::toResponse)
                    .orElseThrow(() -> new ResourceNotFoundException("Workload not found with id: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while fetching workload {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve workload details");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkLoadResponseDto> getWorkLoadsByFaculty(UUID facultyId) {
        try {
            return repository.findByFacultyId(facultyId).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching workloads for faculty {}: {}", facultyId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve faculty workloads");
        }
    }

    @Override
    public WorkLoadResponseDto updateWorkLoad(UUID id, WorkLoadRequestDto request) {
        try {
            WorkLoad existing = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Workload not found with id: " + id));

            if (request.facultyId() != null) existing.setFaculty(userRepository.getReferenceById(request.facultyId()));
            if (request.courseId() != null) existing.setCourse(courseRepository.getReferenceById(request.courseId()));
            if (request.hours() != null) existing.setHours(request.hours());
            if (request.semester() != null) existing.setSemester(request.semester());
            if (request.status() != null) existing.setStatus(request.status());

            WorkLoad updatedWorkLoad = repository.save(existing);
            log.info("Workload record updated successfully: {}", id);
            return mapper.toResponse(updatedWorkLoad);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating workload {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update workload record");
        }
    }

    @Override
    public void deleteWorkLoad(UUID id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException("Workload not found with id: " + id);
            }
            repository.deleteById(id);
            log.info("Workload record deleted successfully: {}", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while deleting workload {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete workload record");
        }
    }
}