package com.cts.edusphere.repositories.faculty;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, UUID> {
    Optional<Faculty> findByEmail(String email);

    Optional<Faculty> findByPhone(String phone);

    List<Faculty> findByDepartmentId(UUID departmentId);

    List<Faculty> findByDepartmentIdAndStatus(UUID departmentId, Status status);

    List<Faculty> findAllByStatus(Status status);
}