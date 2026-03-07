package com.cts.edusphere.controllers.course;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.services.course.CourseService;
import com.cts.edusphere.services.course.CourseServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.descriptor.jdbc.UuidAsBinaryJdbcType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  public ResponseEntity<String> createCourse(
    @Valid @RequestBody CourseRequest courseRequest
  ) {
    try {
      CourseResponse courseResponse = courseService.createCourse(courseRequest);
      return ResponseEntity.ok("Successfully created user");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<CourseResponse>> getAllCourses() {
    try {
      List<CourseResponse> courseResponse = courseService.getAllCourses();
      return ResponseEntity.ok(courseResponse);
    } catch (Exception e) {
      return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<CourseResponse> getCourseById(@PathVariable UUID id) {
    try {
      CourseResponse courseResponse = courseService.getCourseById(id);
      return ResponseEntity.ok(courseResponse);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateCourseById(
    @PathVariable UUID id,
    @Valid @RequestBody CourseRequest courseRequest
  ) {
    try {
      courseService.updateCourse(id, courseRequest);
      return ResponseEntity.ok("Successfully updated User with Id: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<String> setActivate(
    @PathVariable UUID id,
    @Valid @RequestBody Status status
  ) {
    try {
      courseService.setActivateDeactivate(id, status);
      return ResponseEntity.ok(
        "Successfully activated/deactivated User with Id: " + id
      );
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCourseById(@PathVariable UUID id) {
    try {
      courseService.deleteCourseById(id);
      return ResponseEntity.ok("Successfully Deleted User with Id:" + id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
