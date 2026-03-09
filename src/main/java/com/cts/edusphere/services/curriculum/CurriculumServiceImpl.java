package com.cts.edusphere.services.curriculum;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.CurriculumMapper;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.Curriculum;
import com.cts.edusphere.repositories.CourseRepository;
import com.cts.edusphere.repositories.CurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final CourseRepository courseRepository;
    private final CurriculumMapper curriculumMapper;

    @Override
    public CurriculumResponse createCurriculum(CurriculumRequest curriculumRequest) {
        Course course = courseRepository.
                findById(curriculumRequest.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + curriculumRequest.courseId()));
        Curriculum curriculum = Curriculum.builder().
                course(course).
                description(curriculumRequest.description()).
                modulesJSON(curriculumRequest.modulesJSON()).
                status(curriculumRequest.status()).
                build();
        return curriculumMapper.toResponseDto(curriculumRepository.save(curriculum));
    }

    @Override
    public List<CurriculumResponse> getAllCurriculums() {
        return curriculumRepository.findAll()
                .stream()
                .map(curriculumMapper::toResponseDto)
                .toList();
    }

    @Override
    public CurriculumResponse getCurriculumById(UUID id) {
        Curriculum curriculum =  curriculumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Curriculum not found with id: " + id));
        return curriculumMapper.toResponseDto(curriculum);
    }

    @Override
    public void updateCurriculumById(UUID id, CurriculumRequest curriculumRequest) {
        Curriculum curriculum = curriculumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Curriculum not found with id: " + id));
        Course course = courseRepository.findById(curriculumRequest.courseId()).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + curriculumRequest.courseId()));

        if(curriculumRequest.description() != null) {
            curriculum.setDescription(curriculumRequest.description());
        }
        if(curriculumRequest.modulesJSON() != null) {
            curriculum.setModulesJSON(curriculumRequest.modulesJSON());
        }
        if(curriculumRequest.status() != null) {
            curriculum.setStatus(curriculumRequest.status());
        }
        curriculum.setCourse(course);

        curriculumRepository.save(curriculum);
    }

    @Override
    public void deleteCurriculumById(UUID id) {
        if(!curriculumRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curriculum not found with id: " + id);
        }
        curriculumRepository.deleteById(id);
    }
}
