package com.cts.edusphere.services.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;

import java.util.List;
import java.util.UUID;

public interface ThesisService {

    ThesisResponseDto createThesis(ThesisRequestDto request);

    List<ThesisResponseDto> getAllThesis();

    ThesisResponseDto getThesisById(UUID id);

    List<ThesisResponseDto> getThesisByStudent(UUID studentId);

    List<ThesisResponseDto> getThesisBySupervisor(UUID facultyId);

    ThesisResponseDto updateThesis(UUID id, ThesisRequestDto request);

    void deleteThesis(UUID id);
}
