package com.cts.edusphere.services.course;

import java.util.List;
import java.util.UUID;

import com.cts.edusphere.common.dto.course.CourseRequest;
import com.cts.edusphere.common.dto.course.CourseResponse;
import com.cts.edusphere.enums.Status;

/**
 * Service interface defining the contract for course management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, deleting, and changing the
 * activation status of courses. All operations use Data Transfer Objects (DTOs) to
 * decouple the service layer from the underlying persistence model.</p>
 */
public interface CourseService {

    /**
     * Creates a new course from the provided request data.
     *
     * @param courseRequest the {@link CourseRequest} containing the course details; must not be {@code null}
     * @return a {@link CourseResponse} representing the newly created course
     * @throws com.cts.edusphere.exceptions.DuplicateResourceException if a course with the same code or name already exists
     */
    CourseResponse createCourse(CourseRequest courseRequest);

    /**
     * Retrieves all courses in the system.
     *
     * @return a {@link List} of {@link CourseResponse} objects representing all courses;
     *         never {@code null}, may be empty
     */
    List<CourseResponse> getAllCourses();

    /**
     * Retrieves a single course by its unique identifier.
     *
     * @param id the {@link UUID} of the course to retrieve
     * @return a {@link CourseResponse} representing the found course
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no course exists with the given ID
     */
    CourseResponse getCourseById(UUID id);

    /**
     * Updates the details of an existing course.
     *
     * @param id            the {@link UUID} of the course to update
     * @param courseRequest the {@link CourseRequest} containing the updated field values; must not be {@code null}
     * @return a {@link CourseResponse} representing the updated course
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no course exists with the given ID
     */
    CourseResponse updateCourse(UUID id, CourseRequest courseRequest);

    /**
     * Deletes the course identified by the given ID.
     *
     * @param id the {@link UUID} of the course to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no course exists with the given ID
     */
    void deleteCourseById(UUID id);

    /**
     * Activates or deactivates a course by updating its status.
     *
     * @param id    the {@link UUID} of the course whose status is to be changed
     * @param staus the new {@link Status} to apply (e.g., ACTIVE or INACTIVE)
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no course exists with the given ID
     */
    void setActivateDeactivate(UUID id, Status staus);
}
