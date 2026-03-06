package com.cts.edusphere.mappers;

import com.cts.edusphere.common.dto.thesis.ThesisRequest;
import com.cts.edusphere.common.dto.thesis.ThesisResponse;
import com.cts.edusphere.modules.Thesis;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

@Component
public class ThesisMapper {
    public Thesis toEntity(ThesisRequest request) {
        Thesis thesis = new Thesis();
        thesis.setStudent(request.student());
        thesis.setTitle(request.title());
        thesis.setSupervisor(request.supervisor());
        thesis.setStatus(request.status());
        return thesis;
    }

    public ThesisResponse toResponse(Thesis thesis) {
        return new ThesisResponse(
                thesis.getStudent(),
                thesis.getTitle(),
                thesis.getSupervisor(),
                thesis.getSubmissionDate(),
                thesis.getStatus()
        );
    }
}
