package com.cts.edusphere.mappers.curriculum;

import org.springframework.stereotype.Component;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.modules.curriculum.Curriculum;

@Component
public class CurriculumMapper {
    public CurriculumResponse toResponseDto(Curriculum curriculum) {
        if (curriculum == null) {
            return null;
        }

        return new CurriculumResponse(
            curriculum.getId(),
            curriculum.getCourse().getId(),
            curriculum.getDescription(),
            curriculum.getModulesJSON(),
            curriculum.getStatus()
        );
    }

    public Curriculum toEntity(CurriculumRequest curriculumRequest) {
        if (curriculumRequest == null) {
            return null;
        }

        return Curriculum
            .builder()
            .description(curriculumRequest.description())
            .modulesJSON(curriculumRequest.modulesJSON())
            .status(curriculumRequest.status())
            .build();
    }
}
