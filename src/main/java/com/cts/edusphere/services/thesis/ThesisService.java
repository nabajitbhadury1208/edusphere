package com.cts.edusphere.services.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequest;
import com.cts.edusphere.common.dto.thesis.ThesisResponse;

import java.util.List;
import java.util.UUID;

public interface ThesisService {
    ThesisResponse createThesis(ThesisRequest request);
    ThesisResponse getThesisById(UUID id);
    List<ThesisResponse> getThesisByStudent(UUID studentId);
    List<ThesisResponse> getThesisBySupervisor(UUID facultyId);
    ThesisResponse updateThesis(UUID id, ThesisRequest request);
    void deleteThesis(UUID id);
}
