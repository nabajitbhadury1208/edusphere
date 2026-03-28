package com.cts.edusphere.services.thesis;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for thesis management operations within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting thesis records,
 * as well as querying theses by the authoring student or the supervising faculty member.
 * All operations use Data Transfer Objects (DTOs) to decouple the service layer from
 * the underlying persistence model.</p>
 */
public interface ThesisService {

    /**
     * Creates a new thesis record from the provided request data.
     *
     * @param request the {@link ThesisRequestDto} containing the thesis details
     *                (title, abstract, student, supervisor, etc.); must not be {@code null}
     * @return a {@link ThesisResponseDto} representing the newly created thesis record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if the referenced student or supervisor does not exist
     */
    ThesisResponseDto createThesis(ThesisRequestDto request);

    /**
     * Retrieves a single thesis record by its unique identifier.
     *
     * @param id the {@link UUID} of the thesis to retrieve
     * @return a {@link ThesisResponseDto} representing the found thesis record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no thesis exists with the given ID
     */
    ThesisResponseDto getThesisById(UUID id);

    /**
     * Retrieves all thesis records authored by a specific student.
     *
     * @param studentId the {@link UUID} of the student whose theses are to be retrieved
     * @return a {@link List} of {@link ThesisResponseDto} objects for the given student;
     *         never {@code null}, may be empty if the student has no theses
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no student exists with the given ID
     */
    List<ThesisResponseDto> getThesisByStudent(UUID studentId);

    /**
     * Retrieves all thesis records supervised by a specific faculty member.
     *
     * @param facultyId the {@link UUID} of the faculty member (supervisor) whose theses are to be retrieved
     * @return a {@link List} of {@link ThesisResponseDto} objects supervised by the given faculty member;
     *         never {@code null}, may be empty if the faculty member supervises no theses
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no faculty member exists with the given ID
     */
    List<ThesisResponseDto> getThesisBySupervisor(UUID facultyId);

    /**
     * Updates the details of an existing thesis record.
     *
     * @param id      the {@link UUID} of the thesis to update
     * @param request the {@link ThesisRequestDto} containing the updated field values; must not be {@code null}
     * @return a {@link ThesisResponseDto} representing the updated thesis record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no thesis exists with the given ID
     */
    ThesisResponseDto updateThesis(UUID id, ThesisRequestDto request);

    /**
     * Deletes the thesis record identified by the given ID.
     *
     * @param id the {@link UUID} of the thesis to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no thesis exists with the given ID
     */
    void deleteThesis(UUID id);
}
