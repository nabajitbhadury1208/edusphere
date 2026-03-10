package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, UUID> {
    Optional<Faculty> findByEmail(String email);

    Optional<Faculty> findByPhone(String phone);

    List<Faculty> findByDepartmentId(UUID departmentId);

    @Query("SELECT f FROM faculty f WHERE f.department.id = :departmentId AND f.status = 'ACTIVE'")
    List<Faculty> findActiveFacultiesByDepartment(@Param("departmentId") UUID departmentId);

    @Query("SELECT f FROM faculty f WHERE f.status = 'ACTIVE'")
    List<Faculty> findAllActiveFaculties();
}