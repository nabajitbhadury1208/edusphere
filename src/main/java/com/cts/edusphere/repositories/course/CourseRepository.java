package com.cts.edusphere.repositories.course;

import com.cts.edusphere.modules.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    boolean existsByTitle(String title);
}
