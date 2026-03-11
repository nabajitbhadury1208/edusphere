package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.modules.Thesis;
import org.springframework.stereotype.Component;

@Component
public class ThesisMapper {
    public Thesis toEntity(ThesisRequestDto request) {
        Thesis thesis = new Thesis();
        thesis.setStudent(request.student());
        thesis.setTitle(request.title());
        thesis.setSupervisor(request.supervisor());
        thesis.setStatus(request.status());
        return thesis;
    }

    public ThesisResponseDto toResponse(Thesis thesis) {
        return new ThesisResponseDto(
                thesis.getStudent(),
                thesis.getTitle(),
                thesis.getSupervisor(),
                thesis.getSubmissionDate(),
                thesis.getStatus()
        );
    }
}
