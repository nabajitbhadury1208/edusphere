package com.cts.edusphere.repositories.student;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByPhone(String phone);

    List<Student> findAllByStatus(Status status);
}

