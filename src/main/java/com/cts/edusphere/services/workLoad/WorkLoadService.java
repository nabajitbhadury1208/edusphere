package com.cts.edusphere.services.workLoad;

import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining the contract for faculty workload management within EduSphere.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting workload records,
 * as well as querying workloads assigned to a specific faculty member. Workload records
 * capture the teaching and administrative responsibilities allocated to each faculty member.
 * All operations use Data Transfer Objects (DTOs) to decouple the service layer from the
 * underlying persistence model.</p>
 */
public interface WorkLoadService {

    /**
     * Creates a new workload record from the provided request data.
     *
     * @param request the {@link WorkLoadRequestDto} containing the workload details
     *                (faculty, course, hours, semester, etc.); must not be {@code null}
     * @return a {@link WorkLoadResponseDto} representing the newly created workload record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if the referenced faculty member or course does not exist
     */
    WorkLoadResponseDto createWorkLoad(WorkLoadRequestDto request);

    /**
     * Retrieves all workload records in the system.
     *
     * @return a {@link List} of {@link WorkLoadResponseDto} objects representing all workload records;
     *         never {@code null}, may be empty
     */
    List<WorkLoadResponseDto> getAllWorkLoads();

    /**
     * Retrieves a single workload record by its unique identifier.
     *
     * @param id the {@link UUID} of the workload record to retrieve
     * @return a {@link WorkLoadResponseDto} representing the found workload record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no workload record exists with the given ID
     */
    WorkLoadResponseDto getWorkLoadById(UUID id);

    /**
     * Retrieves all workload records assigned to a specific faculty member.
     *
     * @param facultyId the {@link UUID} of the faculty member whose workloads are to be retrieved
     * @return a {@link List} of {@link WorkLoadResponseDto} objects for the given faculty member;
     *         never {@code null}, may be empty if the faculty member has no assigned workloads
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no faculty member exists with the given ID
     */
    List<WorkLoadResponseDto> getWorkLoadsByFaculty(UUID facultyId);

    /**
     * Updates an existing workload record with new values.
     *
     * @param id      the {@link UUID} of the workload record to update
     * @param request the {@link WorkLoadRequestDto} containing the updated field values; must not be {@code null}
     * @return a {@link WorkLoadResponseDto} representing the updated workload record
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no workload record exists with the given ID
     */
    WorkLoadResponseDto updateWorkLoad(UUID id, WorkLoadRequestDto request);

    /**
     * Deletes the workload record identified by the given ID.
     *
     * @param id the {@link UUID} of the workload record to delete
     * @throws com.cts.edusphere.exceptions.ResourceNotFoundException if no workload record exists with the given ID
     */
    void deleteWorkLoad(UUID id);
}
