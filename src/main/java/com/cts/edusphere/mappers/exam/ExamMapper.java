package com.cts.edusphere.mappers.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.exam.Exam;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Exam} entity objects
 * and their corresponding DTO representations ({@link ExamRequest} and {@link ExamResponse}).
 *
 * <p>Both mapping methods are declared {@code static} and can be used without an instance.
 * This class is also a Spring-managed component for injection where needed.</p>
 */
@Component
public class ExamMapper {

    /**
     * Converts an {@link ExamRequest} DTO and a resolved {@link Course} entity to an
     * {@link Exam} entity.
     *
     * <p>If the provided DTO is {@code null}, this method returns {@code null}.
     * The {@link Course} object is provided separately because the request DTO only
     * carries an identifier; the caller is responsible for resolving the course beforehand.</p>
     *
     * @param dto    the {@link ExamRequest} DTO containing exam data; may be {@code null}
     * @param course the {@link Course} entity to associate with the exam
     * @return a new {@link Exam} entity built from the request data and course,
     *         or {@code null} if the DTO is {@code null}
     */
    public static Exam toEntity(ExamRequest dto, Course course){
        if (dto == null) {
            return null;
        }
        return Exam.builder()
                .course(course)
                .type(dto.type())
                .date(dto.date())
                .status(dto.status())
                .build();
    }

    /**
     * Converts an {@link Exam} entity to an {@link ExamResponse} DTO.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The course ID and all audit timestamps are extracted from the entity.</p>
     *
     * @param exam the {@link Exam} entity to convert; may be {@code null}
     * @return an {@link ExamResponse} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
    public static ExamResponse toDTO(Exam exam){
        if (exam == null) {
            return null;
        }
        return ExamResponse.builder()
                .id(exam.getId())
                .courseId(exam.getCourse().getId())
                .type(exam.getType())
                .date(exam.getDate())
                .status(exam.getStatus())
                .createdAt(exam.getCreatedAt())
                .updatedAt(exam.getUpdatedAt())
                .build();
    }

}
