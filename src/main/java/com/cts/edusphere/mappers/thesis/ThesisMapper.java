package com.cts.edusphere.mappers.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.thesis.Thesis;
import org.springframework.stereotype.Component;

@Component
public class ThesisMapper {
    public Thesis toEntity(ThesisRequestDto request) {
          return Thesis.builder()
                  .student(Student.builder().id(request.studentId()).build())
                  .title(request.title())
                  .supervisor(Faculty.builder().id(request.supervisorId()).build())
                  .submissionDate(request.submissionDate())
                  .status(request.status())
                  .build();
    }

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
