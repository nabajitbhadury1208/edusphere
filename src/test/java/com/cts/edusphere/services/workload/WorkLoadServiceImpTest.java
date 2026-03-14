package com.cts.edusphere.services.workload;

import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.WorkLoadMapper;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.modules.WorkLoad;
import com.cts.edusphere.repositories.CourseRepository;
import com.cts.edusphere.repositories.UserRepository;
import com.cts.edusphere.repositories.WorkLoadRepository;
import com.cts.edusphere.services.workLoad.WorkLoadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkLoadServiceImplTest {

    @Mock private WorkLoadRepository repository;
    @Mock private WorkLoadMapper mapper;
    @Mock private UserRepository userRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks
    private WorkLoadServiceImpl workLoadService;

    private UUID workLoadId;
    private UUID facultyId;
    private UUID courseId;
    private WorkLoadRequestDto requestDto;
    private WorkLoad workLoad;
    private WorkLoadResponseDto responseDto;

    @BeforeEach
    void setUp() {
        workLoadId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        requestDto = new WorkLoadRequestDto(facultyId, courseId, 40, "Fall 2026", Status.ACTIVE);

        // This is a real object, not a mock.
        workLoad = WorkLoad.builder()
                .id(workLoadId)
                .hours(40)
                .semester("Fall 2026")
                .status(Status.ACTIVE)
                .build();

        responseDto = new WorkLoadResponseDto(facultyId, courseId, 40, "Fall 2026", Status.ACTIVE);
    }

    // --- CREATE TESTS ---

    @Test
    void createWorkLoad_Success() {
        when(mapper.toEntity(any(WorkLoadRequestDto.class))).thenReturn(workLoad);
        when(courseRepository.getReferenceById(courseId)).thenReturn(mock(Course.class));
        when(userRepository.getReferenceById(facultyId)).thenReturn(mock(User.class));
        when(repository.save(any(WorkLoad.class))).thenReturn(workLoad);
        when(mapper.toResponse(any(WorkLoad.class))).thenReturn(responseDto);

        WorkLoadResponseDto result = workLoadService.createWorkLoad(requestDto);

        assertThat(result).isNotNull();
        verify(repository).save(any(WorkLoad.class));
    }

    @Test
    void createWorkLoad_ThrowsInternalServerError_OnException() {
        when(mapper.toEntity(any())).thenThrow(new RuntimeException("DB Error"));

        assertThatThrownBy(() -> workLoadService.createWorkLoad(requestDto))
                .isInstanceOf(InternalServerErrorException.class);
    }

    // --- GET TESTS ---

    @Test
    void getAllWorkLoads_Success() {
        when(repository.findAll()).thenReturn(List.of(workLoad));
        when(mapper.toResponse(workLoad)).thenReturn(responseDto);

        List<WorkLoadResponseDto> results = workLoadService.getAllWorkLoads();

        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(responseDto);
    }

    @Test
    void getWorkLoadById_Success() {
        when(repository.findById(workLoadId)).thenReturn(Optional.of(workLoad));
        when(mapper.toResponse(workLoad)).thenReturn(responseDto);

        WorkLoadResponseDto result = workLoadService.getWorkLoadById(workLoadId);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getWorkLoadById_NotFound_ThrowsException() {
        when(repository.findById(workLoadId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workLoadService.getWorkLoadById(workLoadId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- UPDATE TESTS ---

    @Test
    void updateWorkLoad_Success() {
        // Arrange
        WorkLoadRequestDto updateDto = new WorkLoadRequestDto(facultyId, courseId, 60, "Spring 2027", Status.ACTIVE);

        when(repository.findById(workLoadId)).thenReturn(Optional.of(workLoad));
        when(userRepository.getReferenceById(facultyId)).thenReturn(mock(User.class));
        when(courseRepository.getReferenceById(courseId)).thenReturn(mock(Course.class));
        when(repository.save(any(WorkLoad.class))).thenReturn(workLoad);
        when(mapper.toResponse(any(WorkLoad.class))).thenReturn(responseDto);

        // Act
        workLoadService.updateWorkLoad(workLoadId, updateDto);

        // Assert - We check the state of the 'workLoad' object directly.
        assertThat(workLoad.getHours()).isEqualTo(60);
        assertThat(workLoad.getSemester()).isEqualTo("Spring 2027");
        assertThat(workLoad.getStatus()).isEqualTo(Status.ACTIVE);

        verify(repository).save(workLoad);
    }

    @Test
    void updateWorkLoad_NotFound_ThrowsException() {
        when(repository.findById(workLoadId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workLoadService.updateWorkLoad(workLoadId, requestDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- DELETE TESTS ---

    @Test
    void deleteWorkLoad_Success() {
        when(repository.existsById(workLoadId)).thenReturn(true);

        workLoadService.deleteWorkLoad(workLoadId);

        verify(repository).deleteById(workLoadId);
    }

    @Test
    void deleteWorkLoad_NotFound_ThrowsException() {
        when(repository.existsById(workLoadId)).thenReturn(false);

        assertThatThrownBy(() -> workLoadService.deleteWorkLoad(workLoadId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}