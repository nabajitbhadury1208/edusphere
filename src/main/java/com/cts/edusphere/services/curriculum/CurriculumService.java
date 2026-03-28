package com.cts.edusphere.services.curriculum;

import java.util.List;
import java.util.UUID;

import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;

/**
 * Service interface defining the contract for curriculum management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting curriculum records.
 * All operations use Data Transfer Objects (DTOs) to decouple the service layer from the
 * underlying persistence model.</p>
 */
public interface CurriculumService {

    /**
     * Creates a new curriculum from the provided request data.
     *
     * @param curriculumRequest the {@link CurriculumRequest} containing the curriculum details; must not be {@code null}
     * @return a {@link CurriculumResponse} representing the newly created curriculum
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException  if a referenced entity (e.g., course or department) does not exist
     * @throws com.cts.edusphere.exceptions.DuplicateResourceException if a curriculum with the same identifying attributes already exists
     */
    CurriculumResponse createCurriculum(CurriculumRequest curriculumRequest);

    /**
     * Retrieves all curriculum records in the system.
     *
     * @return a {@link List} of {@link CurriculumResponse} objects representing all curricula;
     *         never {@code null}, may be empty
     */
    List<CurriculumResponse> getAllCurriculums();

    /**
     * Retrieves a single curriculum record by its unique identifier.
     *
     * @param id the {@link UUID} of the curriculum to retrieve
     * @return a {@link CurriculumResponse} representing the found curriculum
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no curriculum exists with the given ID
     */
    CurriculumResponse getCurriculumById(UUID id);

    /**
     * Updates the details of an existing curriculum record.
     *
     * @param id                the {@link UUID} of the curriculum to update
     * @param curriculumRequest the {@link CurriculumRequest} containing the updated field values; must not be {@code null}
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no curriculum exists with the given ID
     */
    void updateCurriculumById(UUID id, CurriculumRequest curriculumRequest);

    /**
     * Deletes the curriculum record identified by the given ID.
     *
     * @param id the {@link UUID} of the curriculum to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no curriculum exists with the given ID
     */
    void deleteCurriculumById(UUID id);
}
