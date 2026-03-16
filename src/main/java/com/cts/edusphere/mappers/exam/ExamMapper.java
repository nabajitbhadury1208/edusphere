package com.cts.edusphere.mappers.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.exam.Exam;
import org.springframework.stereotype.Component;

@Component
public class ExamMapper {

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

