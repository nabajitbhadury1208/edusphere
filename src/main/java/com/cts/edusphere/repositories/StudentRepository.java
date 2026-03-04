package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByPhone(String phone);

    @Query("SELECT s FROM Student s WHERE s.status = 'ACTIVE'")
    List<Student> findAllActiveStudents();
}

