package com.cts.edusphere.repositories.curriculum;

import com.cts.edusphere.modules.curriculum.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Curriculum} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD and pagination operations
 * for curriculum records. No additional custom query methods are defined beyond
 * the inherited JpaRepository contract.</p>
 */
@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, UUID> {
}
