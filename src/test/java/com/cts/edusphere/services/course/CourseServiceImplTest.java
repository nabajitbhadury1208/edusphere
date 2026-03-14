package com.cts.edusphere.services.course;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.CourseAlreadyExistsException;
import com.cts.edusphere.exceptions.genericexceptions.CourseNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.mappers.courses.CoursesMapper;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.department.Department;
import com.cts.edusphere.repositories.course.CourseRepository;
import com.cts.edusphere.repositories.department.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CoursesMapper coursesMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private CourseRequest courseRequest;
    private CourseResponse courseResponse;
    private UUID courseId;
    private UUID deptId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        deptId = UUID.randomUUID();

        courseRequest = new CourseRequest("Java Fullstack", deptId, 4, 12, Status.ACTIVE);

        course = Course.builder()
                .id(courseId)
                .title("Java Fullstack")
                .credits(4)
                .duration(12)
                .status(Status.ACTIVE)
                .build();

        courseResponse = new CourseResponse(courseId, "Java Fullstack", deptId, "IT", 4, 12, Status.ACTIVE);
    }

    @Test
    void createCourse_ShouldSucceed_WhenTitleIsUnique() {
        when(courseRepository.existsByTitle(courseRequest.title())).thenReturn(false);
        when(coursesMapper.toEntity(courseRequest)).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(coursesMapper.toResponseDto(course)).thenReturn(courseResponse);

        CourseResponse result = courseService.createCourse(courseRequest);

        assertNotNull(result);
        assertEquals(courseRequest.title(), result.title());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_ShouldThrowInternalServerError_WhenCourseExists() {
        when(courseRepository.existsByTitle(courseRequest.title())).thenReturn(true);

        assertThrows(InternalServerErrorException.class, () -> courseService.createCourse(courseRequest));
    }

    @Test
    void getCourseById_ShouldReturnResponse_WhenIdExists() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(coursesMapper.toResponseDto(course)).thenReturn(courseResponse);

        CourseResponse result = courseService.getCourseById(courseId);

        assertEquals(courseId, result.id());
        verify(courseRepository).findById(courseId);
    }

    @Test
    void updateCourse_ShouldUpdateFields_WhenCourseExists() {

        Department dept = new Department();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(dept));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(coursesMapper.toResponseDto(any(Course.class))).thenReturn(courseResponse);

        CourseResponse result = courseService.updateCourse(courseId, courseRequest);

        assertNotNull(result);
        verify(courseRepository).save(course);
    }

    @Test
    void setActivateDeactivate_ShouldUpdateStatus() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        courseService.setActivateDeactivate(courseId, Status.INACTIVE);

        assertEquals(Status.INACTIVE, course.getStatus());
        verify(courseRepository).save(course);
    }

    @Test
    void deleteCourseById_ShouldInvokeDelete_WhenExists() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        courseService.deleteCourseById(courseId);

        verify(courseRepository).delete(course);
    }
}