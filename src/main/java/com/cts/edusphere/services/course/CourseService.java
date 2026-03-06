package com.cts.edusphere.services.course;

import java.util.List;
import java.util.UUID;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.enums.Status;

public interface CourseService {
    CourseResponse createCourse(CourseRequest courseRequest);
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseById(UUID id);
    CourseResponse updateCourse(UUID id, CourseRequest courseRequest);
    void deleteCourseById(UUID id);
    void setActivateDeactivate(UUID id, Status staus);
}
