package com.cts.edusphere.controllers.course;

import com.cts.edusphere.common.dto.ApiResponse;
import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.common.dto.course.StatusRequest;
import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.services.course.CourseService;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<ApiResponse> createCourse(@Validated(OnCreate.class) @RequestBody CourseRequest courseRequest) {
        courseService.createCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Successfully created course", HttpStatus.CREATED.value()));
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
    public ResponseEntity<ApiResponse> updateCourseById(@PathVariable UUID id,
                                                        @Validated(OnUpdate.class) @RequestBody CourseRequest courseRequest) {
        courseService.updateCourse(id, courseRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of("Successfully updated course with id: " + id, HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<ApiResponse> setActivate(@PathVariable UUID id, @RequestBody StatusRequest statusRequest) {
        courseService.setActivateDeactivate(id, statusRequest.status());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of("Successfully " + statusRequest.status() + " course with id: " + id, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCourseById(@PathVariable UUID id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of("Successfully deleted course with id: " + id, HttpStatus.OK.value()));
    }
}
