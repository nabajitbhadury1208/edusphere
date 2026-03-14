package com.cts.edusphere.mappers.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentNotFoundException;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.repositories.department.DepartmentRepository;

@Component
public class CoursesMapper {
    @Autowired
    DepartmentRepository departmentRepository;

    public CourseResponse toResponseDto(Course course) {
        if (course == null) {
            return null;
        }

        return new CourseResponse(course.getId(),course.getTitle(), course.getDepartment().getId(),
                course.getDepartment().getDepartmentName(), course.getCredits(), course.getDuration(),
                course.getStatus());
    }

    public Course toEntity(CourseRequest courseRequest) {
        return Course.builder().title(courseRequest.title())
                .department(departmentRepository.findById(courseRequest.departmentId())
                        .orElseThrow(() -> new DepartmentNotFoundException(
                                "department with id: " + courseRequest.departmentId() + " not found")))
                .credits(courseRequest.credits()).duration(courseRequest.duration()).status(courseRequest.status()).build();
    }
}
