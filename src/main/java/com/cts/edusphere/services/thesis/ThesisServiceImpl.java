package com.cts.edusphere.services.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequest;
import com.cts.edusphere.common.dto.thesis.ThesisResponse;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.ThesisMapper;
import com.cts.edusphere.modules.Thesis;
import com.cts.edusphere.repositories.ThesisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.*;

@Service
@RequiredArgsConstructor

public class ThesisServiceImpl implements  ThesisService{
    private final ThesisRepository thesisRepository;
    private final ThesisMapper thesisMapper;

    @Override
    public ThesisResponse createThesis(ThesisRequest request) {
        try {
            return thesisMapper.toResponse(thesisRepository.save(thesisMapper.toEntity(request)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create thesis", e);
        }
    }

    @Override
    public ThesisResponse getThesisById(UUID id) {
        return thesisRepository.findById(id)
                .map(thesisMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Thesis not found"));
    }

    @Override
    public List<ThesisResponse> getThesisByStudent(UUID studentId) {
        return thesisRepository.findByStudentId(studentId).stream().map(thesisMapper::toResponse).toList();
    }

    @Override
    public List<ThesisResponse> getThesisBySupervisor(UUID facultyId) {
        return thesisRepository.findBySupervisorId(facultyId).stream()
                .map(thesisMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ThesisResponse updateThesis(UUID id, ThesisRequest request) {
        Thesis existing = thesisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Thesis not found: " + id));
        return thesisMapper.toResponse(thesisRepository.save(existing));
    }

    @Override
    public void deleteThesis(UUID id) {
        if (!thesisRepository.existsById(id)) throw new ResourceNotFoundException("Thesis not found");
        thesisRepository.deleteById(id);
    }
}
