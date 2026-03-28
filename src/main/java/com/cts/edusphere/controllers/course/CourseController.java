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
/**
 * REST controller for managing academic courses.
 * Base path: /api/v1/courses
 */
public class CourseController {

    private final CourseService courseService;

    /**
     * Creates a new course in the system.
     * Checks for duplicate title before saving.
     * Accessible by ADMIN and DEPARTMENT_HEAD roles.
     *
     * @param courseRequest the request containing course title, credits, duration, and department ID
     * @return HTTP 201 with a success message confirming the course was created
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> createCourse(@Validated(OnCreate.class) @RequestBody CourseRequest courseRequest) {
        courseService.createCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created course");
    }

    /**
     * Retrieves all courses in the system.
     * Publicly accessible (no role restriction on this endpoint).
     *
     * @return HTTP 200 with a list of all CourseResponse objects
     */
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courseResponse = courseService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courseResponse);
    }

    /**
     * Retrieves a specific course by its unique identifier.
     * Publicly accessible (no role restriction on this endpoint).
     *
     * @param id the UUID of the course to retrieve
     * @return HTTP 200 with the matching CourseResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable UUID id) {
        CourseResponse courseResponse = courseService.getCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(courseResponse);
    }

    /**
     * Updates an existing course's details by its unique identifier.
     * Supports partial updates: only non-null fields are applied.
     * Accessible by ADMIN and DEPARTMENT_HEAD roles.
     *
     * @param id            the UUID of the course to update
     * @param courseRequest the request body containing updated course fields
     * @return HTTP 200 with a success message including the updated course ID
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> updateCourseById(@PathVariable UUID id,
                                                   @Validated(OnUpdate.class) @RequestBody CourseRequest courseRequest) {
        courseService.updateCourse(id, courseRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully updated user with id: " + id);
    }

    /**
     * Activates or deactivates a course by updating its status.
     * Accessible by ADMIN and DEPARTMENT_HEAD roles.
     *
     * @param id     the UUID of the course whose status is to be changed
     * @param status the new Status value (ACTIVE or INACTIVE)
     * @return HTTP 200 with a success message indicating the applied status and course ID
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPARTMENT_HEAD')")
    public ResponseEntity<String> setActivate(@PathVariable UUID id, @RequestBody Status status) {
        courseService.setActivateDeactivate(id, status);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully" + status + "ed User with Id: " + id);
    }

    /**
     * Permanently deletes a course by its unique identifier.
     * Accessible by ADMIN role only.
     *
     * @param id the UUID of the course to delete
     * @return HTTP 200 with a success message confirming the deletion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteCourseById(@PathVariable UUID id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted User with Id:" + id);
    }
}
