package com.cts.edusphere.repositories.research_project;

import com.cts.edusphere.modules.research_project.ResearchProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResearchProjectRepository extends JpaRepository<ResearchProject, UUID> {

}

