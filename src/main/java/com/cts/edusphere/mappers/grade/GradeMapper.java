package com.cts.edusphere.mappers.grade;

import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.modules.exam.Exam;
import com.cts.edusphere.modules.grade.Grade;
import com.cts.edusphere.modules.student.Student;
import org.springframework.stereotype.Component;


/**
 * Mapper component responsible for converting between {@link Grade} entity objects
 * and their corresponding DTO representations ({@link GradeRequest} and {@link GradeResponse}).
 *
 * <p>Both mapping methods are declared {@code static} and can be used without an instance.
 * This class is also a Spring-managed component for injection where needed.</p>
 */
@Component
public class GradeMapper {

    /**
     * Converts a {@link GradeRequest} DTO along with resolved {@link Exam} and
     * {@link Student} entities to a {@link Grade} entity.
     *
     * <p>If the provided DTO is {@code null}, this method returns {@code null}.
     * The {@link Exam} and {@link Student} objects are provided separately because
     * the request DTO only carries identifiers; the caller is responsible for
     * resolving them beforehand.</p>
     *
     * @param dto     the {@link GradeRequest} DTO containing grade data; may be {@code null}
     * @param exam    the {@link Exam} entity to associate with the grade
     * @param student the {@link Student} entity to associate with the grade
     * @return a new {@link Grade} entity built from the request data, exam, and student,
     *         or {@code null} if the DTO is {@code null}
     */
    public static Grade toEntity(GradeRequest dto, Exam exam, Student student){

        if(dto == null){
            return null;
        }
        return Grade.builder()
                .exam(exam)
                .student(student)
                .score(dto.score())
                .grade(dto.grade())
                .status(dto.status())
                .build();
    }

    /**
     * Converts a {@link Grade} entity to a {@link GradeResponse} DTO.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The exam ID, student ID, and audit timestamps are extracted from the entity
     * and its associated objects.</p>
     *
     * @param grade the {@link Grade} entity to convert; may be {@code null}
     * @return a {@link GradeResponse} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
    public static GradeResponse toDTO(Grade grade){

        if (grade == null) {
            return null;
        }
        return GradeResponse.builder()
                .examId(grade.getExam().getId())
                .studentId(grade.getStudent().getId())
                .score(grade.getScore())
                .grade(grade.getGrade())
                .status(grade.getStatus())
                .createdAt(grade.getCreatedAt())
                .updatedAt(grade.getUpdatedAt())
                .build();
    }




}


