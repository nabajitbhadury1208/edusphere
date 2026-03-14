package com.cts.edusphere.services.curriculum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.curriculum.CurriculumMapper;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.curriculum.Curriculum;
import com.cts.edusphere.repositories.course.CourseRepository;
import com.cts.edusphere.repositories.curriculum.CurriculumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CurriculumServiceImplTest {

    @Mock
    private CurriculumRepository curriculumRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CurriculumMapper curriculumMapper;

    @InjectMocks
    private CurriculumServiceImpl curriculumService;

    private Curriculum curriculum;
    private Course course;
    private CurriculumRequest curriculumRequest;
    private CurriculumResponse curriculumResponse;
    private UUID curriculumId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        curriculumId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        course = Course.builder()
                .id(courseId)
                .title("Software Engineering")
                .credits(4)
                .duration(12)
                .status(Status.ACTIVE)
                .build();

        curriculumRequest = new CurriculumRequest(
                courseId,
                "Core Engineering Curriculum",
                "{\"unit1\": \"Requirements\", \"unit2\": \"Design\"}",
                Status.ACTIVE
        );

        curriculum = Curriculum.builder()
                .id(curriculumId)
                .course(course)
                .description("Core Engineering Curriculum")
                .modulesJSON("{\"unit1\": \"Requirements\", \"unit2\": \"Design\"}")
                .status(Status.ACTIVE)
                .build();

        curriculumResponse = new CurriculumResponse(
                curriculumId,
                courseId,
                "Core Engineering Curriculum",
                "{\"unit1\": \"Requirements\", \"unit2\": \"Design\"}",
                Status.ACTIVE
        );
    }

    @Test
    void createCurriculum_ShouldSucceed_WhenCourseExists() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(curriculumRepository.save(any(Curriculum.class))).thenReturn(curriculum);
        when(curriculumMapper.toResponseDto(any(Curriculum.class))).thenReturn(curriculumResponse);

        CurriculumResponse result = curriculumService.createCurriculum(curriculumRequest);

        assertNotNull(result);
        assertEquals(courseId, result.courseId());
        verify(courseRepository).findById(courseId);
        verify(curriculumRepository).save(any(Curriculum.class));
    }

    @Test
    void createCurriculum_ShouldThrowRuntimeException_WhenCourseNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> curriculumService.createCurriculum(curriculumRequest));

        assertTrue(exception.getMessage().contains("Course not found"));
        verify(curriculumRepository, never()).save(any());
    }

    @Test
    void getCurriculumById_ShouldReturnResponse_WhenIdExists() {
        when(curriculumRepository.findById(curriculumId)).thenReturn(Optional.of(curriculum));
        when(curriculumMapper.toResponseDto(curriculum)).thenReturn(curriculumResponse);

        CurriculumResponse result = curriculumService.getCurriculumById(curriculumId);

        assertNotNull(result);
        assertEquals(curriculumId, result.id());
    }

    @Test
    void getAllCurriculums_ShouldReturnListOfResponses() {
        when(curriculumRepository.findAll()).thenReturn(List.of(curriculum));
        when(curriculumMapper.toResponseDto(any(Curriculum.class))).thenReturn(curriculumResponse);

        List<CurriculumResponse> result = curriculumService.getAllCurriculums();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(curriculumRepository).findAll();
    }

    @Test
    void updateCurriculumById_ShouldUpdateFields_WhenCurriculumAndCourseExist() {
        when(curriculumRepository.findById(curriculumId)).thenReturn(Optional.of(curriculum));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(curriculumRepository.save(any(Curriculum.class))).thenReturn(curriculum);

        curriculumService.updateCurriculumById(curriculumId, curriculumRequest);

        verify(curriculumRepository).save(curriculum);
        assertEquals("Core Engineering Curriculum", curriculum.getDescription());
    }

    @Test
    void deleteCurriculumById_ShouldInvokeRepositoryDelete_WhenExists() {
        when(curriculumRepository.existsById(curriculumId)).thenReturn(true);

        curriculumService.deleteCurriculumById(curriculumId);

        verify(curriculumRepository).deleteById(curriculumId);
    }

    @Test
    void deleteCurriculumById_ShouldThrowResourceNotFound_WhenNotExists() {
        when(curriculumRepository.existsById(curriculumId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> curriculumService.deleteCurriculumById(curriculumId));
        verify(curriculumRepository, never()).deleteById(any());
    }
}