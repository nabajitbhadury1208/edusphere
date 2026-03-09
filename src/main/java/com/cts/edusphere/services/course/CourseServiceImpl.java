package com.cts.edusphere.services.course;

import java.util.stream.Collectors;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.CourseAlreadyExistsException;
import com.cts.edusphere.exceptions.genericexceptions.CourseNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.mappers.CoursesMapper;
import com.cts.edusphere.modules.Course;
import com.cts.edusphere.repositories.CourseRepository;
import com.cts.edusphere.repositories.DepartmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService {
  private final CourseRepository courseRepository;
  private final DepartmentRepository departmentRepository;
  private final CoursesMapper coursesMapper;

  @Override
  public CourseResponse createCourse(CourseRequest courseRequest) {
    try {
      if (courseRepository.existsByTitle(courseRequest.title())) {
        throw new CourseAlreadyExistsException("Course with name " + courseRequest.title() + " already exists");
      }
      Course course = courseRepository.save(coursesMapper.toEntity(courseRequest));
      log.info("Created course by name: {}", courseRequest.title());
      return coursesMapper.toResponseDto(course);

    } catch (Exception e) {
      log.error("Error creating course: {}", e.getMessage());
      throw new InternalServerErrorException("Failed to create course");
    }
  }

  @Override
  public List<CourseResponse> getAllCourses() {
    try {
      List<CourseResponse> courserResponse = courseRepository.findAll().stream().map(coursesMapper::toResponseDto)
          .collect(Collectors.toList());

      log.info("Fetched all courses");

      return courserResponse;
    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to get courses");
    }
  }

  @Override
  public CourseResponse getCourseById(UUID id) {
    try {
      Course course = courseRepository.findById(id)
          .orElseThrow(() -> new CourseNotFoundException("Course with id: " + id + "not found"));
      log.info("Course with id: {} fetched", id);

      return coursesMapper.toResponseDto(course);
    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to get course by Id");
    }
  }

  @Override
  public CourseResponse updateCourse(UUID id, CourseRequest courseRequest) {
    try {
      Course existingCourse = courseRepository.findById(id)
          .orElseThrow(() -> new CourseNotFoundException("Course with id: " + id + " doesn't exist"));

      if (courseRequest.title() != null)
        existingCourse.setTitle(courseRequest.title());

      if (courseRequest.departmentId() != null) {
        existingCourse.setDepartment(departmentRepository.findById(courseRequest.departmentId())
            .orElseThrow(() -> new DepartmentNotFoundException(
                "Department with id " + courseRequest.departmentId() + " doesn't exist")));

      }

      if (courseRequest.credits() != null)
        existingCourse.setCredits(courseRequest.credits());

      if (courseRequest.duration() != null)
        existingCourse.setDuration(courseRequest.duration());

      if (courseRequest.status() != null)
        existingCourse.setStatus(courseRequest.status());

      Course updatedCourse = courseRepository.save(existingCourse);
      log.info("Updated course with id: {}", id);

      return coursesMapper.toResponseDto(updatedCourse);
    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to update course");
    }
  }

  @Override
  public void deleteCourseById(UUID id) {
    try {
      Course course = courseRepository.findById(id)
          .orElseThrow(() -> new CourseNotFoundException("Course with id: " + id + " doesn't exist"));
      courseRepository.delete(course);

      log.info("Deleted course with id: {}", id);
    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to delete course");
    }
  }

  @Override
  public void setActivateDeactivate(UUID id, Status status) {
    try {
      Course course = courseRepository.findById(id)
          .orElseThrow(() -> new CourseNotFoundException("Course with id: " + id + " not found"));

      course.setStatus(status);

      courseRepository.save(course);

      log.info("Updated status of course with id: {}", id);
    } catch (Exception e) {
      throw new InternalServerErrorException("Failed to activate/deactivate user");
    }
  }

}
