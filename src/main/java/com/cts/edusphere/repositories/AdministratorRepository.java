package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {
}
