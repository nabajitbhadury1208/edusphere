package com.cts.edusphere.services.workLoad;

import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.WorkLoadCreateFailedException;
import com.cts.edusphere.exceptions.genericexceptions.WorkLoadsFailedToFetchException;
import com.cts.edusphere.exceptions.genericexceptions.WorkLoadNotFoundException;
import com.cts.edusphere.mappers.work_load.WorkLoadMapper;
import com.cts.edusphere.modules.work_load.WorkLoad;
import com.cts.edusphere.repositories.course.CourseRepository;
import com.cts.edusphere.repositories.user.UserRepository;
import com.cts.edusphere.repositories.work_load.WorkLoadRepository;
import com.cts.edusphere.services.audit_log.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link WorkLoadService} that provides workload management operations
 * including creation, retrieval, update, and deletion of faculty workload records.
 *
 * <p>All persistence operations are delegated to {@link com.cts.edusphere.repositories.work_load.WorkLoadRepository}.
 * Entity-to-DTO mapping is handled by {@link com.cts.edusphere.mappers.work_load.WorkLoadMapper}.
 * The class is marked {@code @Transactional} at the class level; individual read-only
 * methods override this with {@code readOnly = true} for optimised database access.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WorkLoadServiceImpl implements WorkLoadService {

    private final WorkLoadRepository repository;
    private final WorkLoadMapper mapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final AuditLogService auditLogService;


    /**
     * Creates a new workload record from the provided request data.
     *
     * <p>If a {@code courseId} or {@code facultyId} is present in the request, the
     * corresponding {@link com.cts.edusphere.modules.work_load.WorkLoad} entity is linked
     * to those existing records via repository references before being saved.</p>
     *
     * @param request a {@link WorkLoadRequestDto} containing workload details such as hours,
     *                semester, status, facultyId, and courseId; must not be {@code null}
     * @return a {@link WorkLoadResponseDto} representing the newly created workload record
     * @throws WorkLoadCreateFailedException if the workload record cannot be created
     * @throws InternalServerErrorException  if an unexpected error occurs during creation
     */
    @Override
    public WorkLoadResponseDto createWorkLoad(WorkLoadRequestDto request) {
        try {
            WorkLoad workLoad = mapper.toEntity(request);
            if (request.courseId() != null) {
                workLoad.setCourse(courseRepository.getReferenceById(request.courseId()));
            }
            if (request.facultyId() != null) {
                workLoad.setFaculty(userRepository.getReferenceById(request.facultyId()));
            }
            WorkLoad savedWorkLoad = repository.save(workLoad);

            log.info("Workload record created successfully with ID: {}", savedWorkLoad.getId());
            return mapper.toResponse(savedWorkLoad);
        }
        
        catch(WorkLoadCreateFailedException e) {
            log.error("Workload creation failed: {}", e.getMessage());
            throw new WorkLoadCreateFailedException("Failed to create workload record: " + e.getMessage());
        }

        catch (Exception e) {
            log.error("Error occurred while creating workload: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create workload record: " + e.getMessage());
        }
    }

    /**
     * Retrieves all workload records from the system.
     *
     * @return a {@link List} of {@link WorkLoadResponseDto} objects representing all workloads;
     *         never {@code null}, but may be empty if no records exist
     * @throws WorkLoadsFailedToFetchException if the retrieval operation fails
     * @throws InternalServerErrorException    if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkLoadResponseDto> getAllWorkLoads() {
        try {
            return repository.findAll().stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } 
        
        catch(WorkLoadsFailedToFetchException e) {
            log.error("Failed to fetch workloads: {}", e.getMessage());
            throw new WorkLoadsFailedToFetchException("Failed to retrieve workloads list: " + e.getMessage());
        }
        
        catch (Exception e) {
            log.error("Error occurred while fetching all workloads: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve workloads list");
        }
    }
    
    /**
     * Retrieves a single workload record by its unique identifier.
     *
     * @param id the {@link UUID} of the workload to retrieve
     * @return a {@link WorkLoadResponseDto} representing the found workload record
     * @throws WorkLoadNotFoundException       if no workload exists with the specified {@code id}
     * @throws WorkLoadsFailedToFetchException if a retrieval-specific error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public WorkLoadResponseDto getWorkLoadById(UUID id) {
        try {
            return repository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new WorkLoadNotFoundException("Workload not found with id: " + id));

        } catch (WorkLoadsFailedToFetchException e) {
            log.error("Workload not found: {}", e.getMessage());
            throw new WorkLoadsFailedToFetchException("Workload not found with id: " + id);

        } catch (Exception e) {
            log.error("Error occurred while fetching workload {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve workload details");
        }
    }

    /**
     * Retrieves all workload records assigned to a specific faculty member.
     *
     * @param facultyId the {@link UUID} of the faculty member whose workloads are to be fetched
     * @return a {@link List} of {@link WorkLoadResponseDto} objects representing the faculty's
     *         workloads; never {@code null}, but may be empty if none are assigned
     * @throws WorkLoadsFailedToFetchException if the retrieval operation fails
     * @throws InternalServerErrorException    if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkLoadResponseDto> getWorkLoadsByFaculty(UUID facultyId) {
        try {
            return repository.findByFacultyId(facultyId).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } 
        
        catch(WorkLoadsFailedToFetchException e) {
            log.error("Failed to fetch workloads for faculty {}: {}", facultyId, e.getMessage());
            throw new WorkLoadsFailedToFetchException("Failed to retrieve faculty workloads: " + e.getMessage());
        }

        catch (Exception e) {
            log.error("Error fetching workloads for faculty {}: {}", facultyId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve faculty workloads");
        }
    }

    /**
     * Updates an existing workload record identified by the given {@code id}.
     *
     * <p>Only non-null fields in {@code request} are applied; null fields leave the
     * existing values unchanged. Supported updatable fields are {@code facultyId},
     * {@code courseId}, {@code hours}, {@code semester}, and {@code status}.</p>
     *
     * @param id      the {@link UUID} of the workload record to update
     * @param request a {@link WorkLoadRequestDto} containing the fields to update;
     *                fields that are {@code null} are ignored
     * @return a {@link WorkLoadResponseDto} representing the updated workload record
     * @throws ResourceNotFoundException       if no workload exists with the specified {@code id}
     * @throws WorkLoadsFailedToFetchException if an update-related fetch error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during the update
     */
    @Override
    public WorkLoadResponseDto updateWorkLoad(UUID id, WorkLoadRequestDto request) {
        try {
            WorkLoad existing = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Workload not found with id: " + id));

            if (request.facultyId() != null) existing.setFaculty(userRepository.getReferenceById(request.facultyId()));
            if (request.courseId() != null) existing.setCourse(courseRepository.getReferenceById(request.courseId()));
            if (request.hours() != null) existing.setHours(request.hours());
            if (request.semester() != null) existing.setSemester(request.semester());
            if (request.status() != null) existing.setStatus(request.status());

            WorkLoad updatedWorkLoad = repository.save(existing);
            log.info("Workload record updated successfully: {}", id);
            return mapper.toResponse(updatedWorkLoad);

        }  catch (WorkLoadsFailedToFetchException e) {
            log.error("Failed to update workload {}: {}", id, e.getMessage());
            throw new WorkLoadsFailedToFetchException("Failed to update workload: " + e.getMessage());
            
        } catch (Exception e) {
            log.error("Error occurred while updating workload {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update workload record");
        }
    }
    
    /**
     * Deletes the workload record identified by the given {@code id}.
     *
     * <p>Existence is verified before deletion; if no record with the specified
     * {@code id} is found, a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param id the {@link UUID} of the workload record to delete
     * @throws ResourceNotFoundException       if no workload exists with the specified {@code id}
     * @throws WorkLoadsFailedToFetchException if a deletion-related error occurs
     * @throws InternalServerErrorException    if an unexpected error occurs during deletion
     */
    @Override
    public void deleteWorkLoad(UUID id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException("Workload not found with id: " + id);
            }
            repository.deleteById(id);
            log.info("Workload record deleted successfully: {}", id);

        } catch(WorkLoadsFailedToFetchException e) {
            log.error("Failed to delete workload {}: {}", id, e.getMessage());
            throw new WorkLoadsFailedToFetchException("Failed to delete workload: " + e.getMessage());
        }
        
         catch (Exception e) {
            log.error("Error occurred while deleting workload {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete workload record");
        }
    }
}