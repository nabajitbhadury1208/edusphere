package com.cts.edusphere.services.workLoad;

import com.cts.edusphere.common.dto.workload.WorkLoadRequest;
import com.cts.edusphere.common.dto.workload.WorkLoadResponse;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.WorkLoadMapper;
import com.cts.edusphere.modules.WorkLoad;
import com.cts.edusphere.repositories.WorkLoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class WorkLoadServiceImpl implements WorkLoadService {

    private final WorkLoadRepository repository;
    private final WorkLoadMapper mapper;

    @Override
    @Transactional
    public WorkLoadResponse createWorkLoad(WorkLoadRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public List<WorkLoadResponse> getAllWorkLoads() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public WorkLoadResponse getWorkLoadById(UUID id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Workload not found: " + id));
    }

    @Override
    @Transactional
    public List<WorkLoadResponse> getWorkLoadsByFaculty(UUID facultyId) {

        return repository.findByFacultyId(facultyId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkLoadResponse updateWorkLoad(UUID id, WorkLoadRequest request) {
        var existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workload not found: " + id));
        existing.setFaculty(request.faculty());
        existing.setCourse(request.course());
        existing.setHours(request.hours());
        existing.setSemester(request.semester());
        existing.setStatus(request.status());
        return mapper.toResponse(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteWorkLoad(UUID id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Workload not found");
        repository.deleteById(id);
    }
}
