package com.cts.edusphere.services.thesis;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ThesisCreationFailedException;
import com.cts.edusphere.exceptions.genericexceptions.ThesisDeletionFailedException;
import com.cts.edusphere.exceptions.genericexceptions.ThesisNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ThesisUpdationFailedException;
import com.cts.edusphere.mappers.thesis.ThesisMapper;
import com.cts.edusphere.modules.thesis.Thesis;
import com.cts.edusphere.repositories.thesis.ThesisRepository;
import com.cts.edusphere.repositories.student.StudentRepository;
import com.cts.edusphere.repositories.faculty.FacultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing thesis records within the EduSphere platform.
 *
 * <p>Provides CRUD operations for {@link com.cts.edusphere.modules.thesis.Thesis} entities,
 * including creation, retrieval by ID, retrieval by student or supervisor, update, and deletion.
 * All write operations participate in the default {@link org.springframework.transaction.annotation.Transactional}
 * context declared at the class level, while read operations use read-only transactions for
 * optimised performance. Compliance-sensitive operations are additionally decorated with
 * {@link com.cts.edusphere.aspects.ComplianceAudit} to satisfy institutional audit requirements.</p>
 *
 * @see ThesisService
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ThesisServiceImpl implements ThesisService {

    private final ThesisRepository thesisRepository;
    private final ThesisMapper thesisMapper;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    /**
     * Creates a new thesis record from the supplied request data.
     *
     * <p>Maps the incoming {@link ThesisRequestDto} to a {@link Thesis} entity, resolves the
     * optional student and supervisor associations via JPA proxy references, persists the entity,
     * and returns the saved state as a {@link ThesisResponseDto}.
     * This operation is subject to compliance auditing under
     * {@link AuditEntityType#RESEARCH_APPROVAL}.</p>
     *
     * @param request the DTO containing title, status, and optional {@code studentId} /
     *                {@code supervisorId} fields; must not be {@code null}
     * @return a {@link ThesisResponseDto} representing the newly created thesis record
     * @throws ThesisCreationFailedException if a domain-level creation failure is detected
     * @throws InternalServerErrorException  if any unexpected error occurs during persistence
     */
    @Override
    @ComplianceAudit(entityType = AuditEntityType.RESEARCH_APPROVAL, scope = "Verify that the research topic and supervisor assignment follow departmental policy")
    public ThesisResponseDto createThesis(ThesisRequestDto request) {
        try {
            Thesis thesis = thesisMapper.toEntity(request);
            if (request.studentId() != null) {
                thesis.setStudent(studentRepository.getReferenceById(request.studentId()));
            }

            if (request.supervisorId() != null) {
                thesis.setSupervisor(facultyRepository.getReferenceById(request.supervisorId()));
            }

            Thesis savedThesis = thesisRepository.save(thesis);
            log.info("Thesis created successfully with ID: {}", savedThesis.getId());
            return thesisMapper.toResponse(savedThesis);

        } catch (ThesisCreationFailedException e) {
            log.error("Error occurred while creating thesis: {}", e.getMessage());
            throw new ThesisCreationFailedException("Failed to create thesis record: " + e.getMessage());

        } catch (Exception e) {
            log.error("Error occurred while creating thesis: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to create thesis record: " + e.getMessage());
        }
    }

    /**
     * Retrieves a single thesis record by its unique identifier.
     *
     * <p>Executes within a read-only transaction. Throws {@link ThesisNotFoundException} when
     * no thesis exists for the provided {@code id}.</p>
     *
     * @param id the {@link UUID} of the thesis to retrieve; must not be {@code null}
     * @return a {@link ThesisResponseDto} containing the thesis details
     * @throws ThesisNotFoundException      if no thesis with the given {@code id} exists
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public ThesisResponseDto getThesisById(UUID id) {
        try {
            return thesisRepository.findById(id)
                    .map(thesisMapper::toResponse)
                    .orElseThrow(() -> new ThesisNotFoundException("Thesis not found with id: " + id));

        } catch (ThesisNotFoundException e) {
            log.error("Thesis with ID {} not found: {}", id, e.getMessage());
            throw new ThesisNotFoundException("Thesis with ID: " + id + " not found");

        } catch (Exception e) {
            log.error("Error occurred while fetching thesis {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve thesis details");
        }
    }

    /**
     * Retrieves all thesis records associated with a given student.
     *
     * <p>Executes within a read-only transaction. Returns an empty list when the student has no
     * thesis records rather than throwing an exception, unless a domain-specific
     * {@link ThesisNotFoundException} is raised by the repository layer.</p>
     *
     * @param studentId the {@link UUID} of the student whose theses are to be retrieved;
     *                  must not be {@code null}
     * @return an unmodifiable {@link List} of {@link ThesisResponseDto} objects (may be empty)
     * @throws ThesisNotFoundException      if the repository explicitly signals that no theses
     *                                      exist for the student
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public List<ThesisResponseDto> getThesisByStudent(UUID studentId) {
        try {
            return thesisRepository.findByStudentId(studentId).stream()
                    .map(thesisMapper::toResponse)
                    .collect(Collectors.toList());
        } catch(ThesisNotFoundException e) {
            log.error("Thesis for student {} not found: {}", studentId, e.getMessage());
            throw new ThesisNotFoundException("Thesis for student with ID: " + studentId + " not found");
        }
        
        catch (Exception e) {
            log.error("Error fetching thesis for student {}: {}", studentId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve student thesis");
        }
    }

    /**
     * Retrieves all thesis records supervised by a given faculty member.
     *
     * <p>Executes within a read-only transaction. Returns an empty list when the supervisor has
     * no associated thesis records, unless the repository layer raises a
     * {@link ThesisNotFoundException}.</p>
     *
     * @param facultyId the {@link UUID} of the faculty member (supervisor) whose theses are to
     *                  be retrieved; must not be {@code null}
     * @return an unmodifiable {@link List} of {@link ThesisResponseDto} objects (may be empty)
     * @throws ThesisNotFoundException      if the repository explicitly signals that no theses
     *                                      exist for the supervisor
     * @throws InternalServerErrorException if an unexpected error occurs during retrieval
     */
    @Override
    @Transactional(readOnly = true)
    public List<ThesisResponseDto> getThesisBySupervisor(UUID facultyId) {
        try {
            return thesisRepository.findBySupervisorId(facultyId).stream()
                    .map(thesisMapper::toResponse)
                    .collect(Collectors.toList());
        } 
        
        catch(ThesisNotFoundException e) {
            log.error("Thesis for supervisor {} not found: {}", facultyId, e.getMessage());
            throw new ThesisNotFoundException("Thesis for supervisor with ID: " + facultyId + " not found");
        }
        
        catch (Exception e) {
            log.error("Error fetching thesis for supervisor {}: {}", facultyId, e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve supervisor thesis");
        }
    }

    /**
     * Updates an existing thesis record with the non-null fields from the supplied request.
     *
     * <p>Loads the existing {@link Thesis} entity by {@code id}, applies partial updates for
     * {@code studentId}, {@code supervisorId}, {@code title}, and {@code status} if the
     * corresponding request fields are non-null, then persists and returns the updated state.
     * Inherits the class-level {@link org.springframework.transaction.annotation.Transactional}
     * context.</p>
     *
     * @param id      the {@link UUID} of the thesis to update; must not be {@code null}
     * @param request a {@link ThesisRequestDto} containing the fields to update; fields that
     *                are {@code null} are left unchanged
     * @return a {@link ThesisResponseDto} representing the updated thesis record
     * @throws ThesisNotFoundException       if no thesis with the given {@code id} exists
     * @throws ThesisUpdationFailedException if a domain-level update failure is detected
     * @throws InternalServerErrorException  if any unexpected error occurs during persistence
     */
    @Override
    public ThesisResponseDto updateThesis(UUID id, ThesisRequestDto request) {
        try {
            Thesis existing = thesisRepository.findById(id)
                    .orElseThrow(() -> new ThesisNotFoundException("Thesis not found with id: " + id));

            // Update associations using proxies
            if (request.studentId() != null) {
                existing.setStudent(studentRepository.getReferenceById(request.studentId()));
            }
            if (request.supervisorId() != null) {
                existing.setSupervisor(facultyRepository.getReferenceById(request.supervisorId()));
            }

            // Update other basic fields
            if (request.title() != null) existing.setTitle(request.title());
            if (request.status() != null) existing.setStatus(request.status());

            Thesis updatedThesis = thesisRepository.save(existing);
            log.info("Thesis record updated successfully: {}", id);
            return thesisMapper.toResponse(updatedThesis);

        } catch (ThesisUpdationFailedException e) {
            log.error("Error occurred while updating thesis {}: {}", id, e.getMessage());
            throw new ThesisUpdationFailedException("Failed to update thesis record: " + e.getMessage());

        } catch (Exception e) {
            log.error("Error occurred while updating thesis {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to update thesis record");
        }
    }

    /**
     * Deletes a thesis record identified by the given {@code id}.
     *
     * <p>Verifies existence before deletion to provide a meaningful error message when the
     * record is absent. Inherits the class-level
     * {@link org.springframework.transaction.annotation.Transactional} context.</p>
     *
     * @param id the {@link UUID} of the thesis to delete; must not be {@code null}
     * @throws ThesisNotFoundException       if no thesis with the given {@code id} exists
     * @throws ThesisDeletionFailedException if a domain-level deletion failure is detected
     * @throws InternalServerErrorException  if any unexpected error occurs during deletion
     */
    @Override
    public void deleteThesis(UUID id) {
        try {
            if (!thesisRepository.existsById(id)) {
                throw new ThesisNotFoundException("Thesis not found with id: " + id);
            }
            thesisRepository.deleteById(id);
            log.info("Thesis record deleted successfully: {}", id);
            
        } catch (ThesisDeletionFailedException e) {
            log.error("Error occurred while deleting thesis {}: {}", id, e.getMessage());
            throw new ThesisDeletionFailedException("Failed to delete thesis record: " + e.getMessage());

        } catch (Exception e) {
            log.error("Error occurred while deleting thesis {}: {}", id, e.getMessage());
            throw new InternalServerErrorException("Failed to delete thesis record");
        }
    }
}