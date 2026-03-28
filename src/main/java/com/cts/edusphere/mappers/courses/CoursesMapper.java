package com.cts.edusphere.mappers.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentNotFoundException;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.repositories.department.DepartmentRepository;

/**
 * Mapper component responsible for converting between {@link Course} entity objects
 * and their corresponding DTO representations ({@link CourseRequest} and {@link CourseResponse}).
 *
 * <p>This mapper requires access to the {@link DepartmentRepository} to resolve department
 * references during entity creation. It is a Spring-managed component and participates
 * in dependency injection.</p>
 */
@Component
public class CoursesMapper {

    /** Repository used to look up {@link com.cts.edusphere.modules.department.Department} entities by ID. */
    @Autowired
    DepartmentRepository departmentRepository;

    /**
     * Converts a {@link Course} entity to a {@link CourseResponse} DTO.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The department ID and name are extracted from the associated department.</p>
     *
     * @param course the {@link Course} entity to convert; may be {@code null}
     * @return a {@link CourseResponse} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
    public CourseResponse toResponseDto(Course course) {
        if (course == null) {
            return null;
        }

        return new CourseResponse(course.getId(),course.getTitle(), course.getDepartment().getId(),
                course.getDepartment().getDepartmentName(), course.getCredits(), course.getDuration(),
                course.getStatus());
    }

    /**
     * Converts a {@link CourseRequest} DTO to a {@link Course} entity.
     *
     * <p>The department is resolved by looking up the department ID from the request
     * in the {@link DepartmentRepository}. If no department is found with that ID,
     * a {@link DepartmentNotFoundException} is thrown.</p>
     *
     * @param courseRequest the {@link CourseRequest} DTO containing course data to map
     * @return a new {@link Course} entity built from the request data
     * @throws DepartmentNotFoundException if no department exists with the given department ID
     */
    public Course toEntity(CourseRequest courseRequest) {
        return Course.builder().title(courseRequest.title())
                .department(departmentRepository.findById(courseRequest.departmentId())
                        .orElseThrow(() -> new DepartmentNotFoundException(
                                "department with id: " + courseRequest.departmentId() + " not found")))
                .credits(courseRequest.credits()).duration(courseRequest.duration()).status(courseRequest.status()).build();
    }
}
