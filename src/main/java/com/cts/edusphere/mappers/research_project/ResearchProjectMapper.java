package com.cts.edusphere.mappers.research_project;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.research_project.ResearchProject;
import com.cts.edusphere.modules.student.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Mapper component responsible for converting between {@link ResearchProject} entity objects
 * and their corresponding DTO representations ({@link ResearchProjectRequest} and
 * {@link ResearchProjectResponse}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever research project
 * mapping is required. Resolved entity references for faculty lead, faculty members, and
 * participating students must be supplied by the caller during entity creation.</p>
 */
@Component
public class ResearchProjectMapper {

    /**
     * Converts a {@link ResearchProjectRequest} DTO along with resolved entity references to
     * a {@link ResearchProject} entity.
     *
     * <p>The faculty lead, associated faculty members, and participating students must be
     * resolved by the caller before invoking this method, as the request DTO only carries
     * their identifiers.</p>
     *
     * @param request     the {@link ResearchProjectRequest} DTO containing project data
     * @param leadFaculty the {@link Faculty} entity acting as the project lead
     * @param members     the list of {@link Faculty} entities associated with the project
     * @param students    the list of {@link Student} entities participating in the project
     * @return a new {@link ResearchProject} entity built from the provided data
     */
    public ResearchProject toEntity(ResearchProjectRequest request, Faculty leadFaculty, List<Faculty> members, List<Student> students) {
        return ResearchProject.builder()
                .title(request.title())
                .facultyLead(leadFaculty)
                .associatedFacultyMembers(members)
                .participatedStudents(students)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .status(request.status())
                .build();
    }

    /**
     * Converts a {@link ResearchProject} entity to a {@link ResearchProjectResponse} DTO.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The IDs of all associated faculty members and participating students are extracted
     * and included in the response as lists of {@link UUID} values.</p>
     *
     * @param project the {@link ResearchProject} entity to convert; may be {@code null}
     * @return a {@link ResearchProjectResponse} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
    public ResearchProjectResponse toResponse(ResearchProject project) {
        if (project == null) return null;

        List<UUID> facultyIds = project.getAssociatedFacultyMembers().stream()
                .map(Faculty::getId)
                .toList();

        List<UUID> studentIds = project.getParticipatedStudents().stream()
                .map(Student::getId)
                .toList();

        return new ResearchProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getFacultyLead().getId(),
                project.getStatus(),
                project.getEndDate(),
                project.getStartDate(),
                facultyIds,
                studentIds
        );
    }
}
