package com.cts.edusphere.mappers.grade;

import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import com.cts.edusphere.modules.exam.Exam;
import com.cts.edusphere.modules.grade.Grade;
import com.cts.edusphere.modules.student.Student;
import org.springframework.stereotype.Component;


@Component
public class GradeMapper {
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
    public static GradeResponse toDTO(Grade grade){
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


