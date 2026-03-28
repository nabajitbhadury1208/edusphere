package com.cts.edusphere.mappers.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.thesis.Thesis;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Thesis} entity objects
 * and their corresponding DTO representations ({@link ThesisRequestDto} and
 * {@link ThesisResponseDto}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever thesis
 * mapping is required. Relationships to {@link Student} and {@link Faculty} are
 * established using stub entities carrying only the requested IDs.</p>
 */
@Component
public class ThesisMapper {

    /**
     * Converts a {@link ThesisRequestDto} to a {@link Thesis} entity.
     *
     * <p>The student and supervisor associations are set using lightweight stub entities
     * that contain only the IDs supplied in the request. The persistence layer is
     * responsible for resolving these references to fully-loaded entities.</p>
     *
     * @param request the {@link ThesisRequestDto} containing thesis data to map
     * @return a new {@link Thesis} entity built from the request data
     */
    public Thesis toEntity(ThesisRequestDto request) {
          return Thesis.builder()
                  .student(Student.builder().id(request.studentId()).build())
                  .title(request.title())
                  .supervisor(Faculty.builder().id(request.supervisorId()).build())
                  .submissionDate(request.submissionDate())
                  .status(request.status())
                  .build();
    }

    /**
     * Converts a {@link Thesis} entity to a {@link ThesisResponseDto}.
     *
     * <p>The student ID and supervisor ID are extracted from the associated entities
     * embedded in the thesis.</p>
     *
     * @param thesis the {@link Thesis} entity to convert
     * @return a {@link ThesisResponseDto} populated with data from the entity
     */
    public ThesisResponseDto toResponse(Thesis thesis) {
        return new ThesisResponseDto(
                thesis.getStudent().getId(),
                thesis.getTitle(),
                thesis.getSupervisor().getId(),
                thesis.getSubmissionDate(),
                thesis.getStatus()
        );
    }
}
