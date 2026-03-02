package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(@Email String email);
    Optional<User> findByEmail(String email);
}
