package com.cts.edusphere.controllers.course;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.services.course.CourseService;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> createCourse(@Validated(OnCreate.class) @RequestBody CourseRequest courseRequest) {
        courseService.createCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created course");
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courseResponse = courseService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courseResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable UUID id) {
        CourseResponse courseResponse = courseService.getCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(courseResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> updateCourseById(@PathVariable UUID id,
                                                   @Validated(OnUpdate.class) @RequestBody CourseRequest courseRequest) {
        courseService.updateCourse(id, courseRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully updated user with id: " + id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> setActivate(@PathVariable UUID id, @RequestBody Status status) {
        courseService.setActivateDeactivate(id, status);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully" + status + "ed User with Id: " + id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteCourseById(@PathVariable UUID id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted User with Id:" + id);
    }
}
