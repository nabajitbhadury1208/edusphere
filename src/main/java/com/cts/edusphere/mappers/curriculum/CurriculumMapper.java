package com.cts.edusphere.mappers.curriculum;

import org.springframework.stereotype.Component;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.modules.curriculum.Curriculum;

/**
 * Mapper component responsible for converting between {@link Curriculum} entity objects
 * and their corresponding DTO representations ({@link CurriculumRequest} and
 * {@link CurriculumResponse}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever curriculum
 * mapping is required.</p>
 */
@Component
public class CurriculumMapper {

    /**
     * Converts a {@link Curriculum} entity to a {@link CurriculumResponse} DTO.
     *
     * <p>If the provided entity is {@code null}, this method returns {@code null}.
     * The associated course ID is extracted from the curriculum's linked course.</p>
     *
     * @param curriculum the {@link Curriculum} entity to convert; may be {@code null}
     * @return a {@link CurriculumResponse} populated with data from the entity,
     *         or {@code null} if the input is {@code null}
     */
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

    /**
     * Converts a {@link CurriculumRequest} DTO to a {@link Curriculum} entity.
     *
     * <p>If the provided request is {@code null}, this method returns {@code null}.
     * Note that the course association is not set here and must be assigned separately
     * after entity creation.</p>
     *
     * @param curriculumRequest the {@link CurriculumRequest} DTO containing the data to map;
     *                          may be {@code null}
     * @return a new {@link Curriculum} entity built from the request data,
     *         or {@code null} if the input is {@code null}
     */
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
