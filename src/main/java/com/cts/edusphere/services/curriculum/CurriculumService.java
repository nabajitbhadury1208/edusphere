package com.cts.edusphere.services.curriculum;

import java.util.List;
import java.util.UUID;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;

public interface CurriculumService {
    CurriculumResponse createCurriculum(CurriculumRequest curriculumRequest);
    List<CurriculumResponse> getAllCurriculums();
    CurriculumResponse getCurriculumById(UUID id);
    void updateCurriculumById(UUID id, CurriculumRequest curriculumRequest);
    void deleteCurriculumById(UUID id);    
}
