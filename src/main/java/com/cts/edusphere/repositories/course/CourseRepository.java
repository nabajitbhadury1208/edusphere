package com.cts.edusphere.repositories.course;

import com.cts.edusphere.modules.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Course} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations, along with
 * a custom existence check by course title.</p>
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    /**
     * Checks whether a course with the given title already exists in the database.
     *
     * <p>Used for duplicate-title validation before creating a new course.</p>
     *
     * @param title the title of the course to check for existence
     * @return {@code true} if a course with the specified title exists; {@code false} otherwise
     */
    boolean existsByTitle(String title);
}
