package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ThesisRepository extends JpaRepository<Thesis, UUID> {
}

