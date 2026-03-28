package com.cts.edusphere.repositories.research_project;

import com.cts.edusphere.modules.research_project.ResearchProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link ResearchProject} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD and pagination operations
 * for research project records. No additional custom query methods are defined beyond
 * the inherited JpaRepository contract.</p>
 */
@Repository
public interface ResearchProjectRepository extends JpaRepository<ResearchProject, UUID> {

}
