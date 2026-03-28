package com.cts.edusphere.services.curriculum;

import com.cts.edusphere.aspects.ComplianceAudit;
import com.cts.edusphere.common.dto.curriculum.CurriculumRequest;
import com.cts.edusphere.common.dto.curriculum.CurriculumResponse;
import com.cts.edusphere.enums.AuditEntityType;
import com.cts.edusphere.exceptions.genericexceptions.CourseNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.CurriculumNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.CurriculumNotDeletedException;
import com.cts.edusphere.exceptions.genericexceptions.CurriculumNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.CurriculumNotUpdatedException;
import com.cts.edusphere.exceptions.genericexceptions.CurriculumsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.mappers.curriculum.CurriculumMapper;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.curriculum.Curriculum;
import com.cts.edusphere.repositories.course.CourseRepository;
import com.cts.edusphere.repositories.curriculum.CurriculumRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CurriculumService} that provides business logic for managing curricula.
 *
 * <p>Supports creating, retrieving, updating, and deleting {@link Curriculum} entities.
 * Each curriculum is linked to an existing {@link Course}. Compliance auditing is applied
 * to creation operations via the {@code @ComplianceAudit} aspect.
 * Dependencies are injected via constructor by Lombok's {@code @RequiredArgsConstructor}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {
  private final CurriculumRepository curriculumRepository;
  private final CourseRepository courseRepository;
  private final CurriculumMapper curriculumMapper;

  /**
   * Creates a new curriculum associated with an existing course.
   *
   * <p>Validates that the course referenced by {@code curriculumRequest.courseId()} exists
   * before building and persisting the {@link Curriculum} entity. This operation is audited
   * for compliance via the {@code @ComplianceAudit} aspect with entity type
   * {@link AuditEntityType#CURRICULUM_CREATED}.
   *
   * @param curriculumRequest the request DTO containing the course ID, description,
   *                          modules JSON, and status for the new curriculum
   * @return a {@link CurriculumResponse} representing the newly created curriculum
   * @throws CourseNotFoundException       if no course exists with the ID provided in the request
   * @throws CurriculumNotCreatedException if the curriculum could not be created due to a persistence error
   * @throws InternalServerErrorException  if an unexpected error occurs during creation
   */
  @Override
  @ComplianceAudit(
      entityType = AuditEntityType.CURRICULUM_CREATED,
      scope = "Verify if new course meets educational standards")
  public CurriculumResponse createCurriculum(CurriculumRequest curriculumRequest) {
    try {
      Course course =
          courseRepository
              .findById(curriculumRequest.courseId())
              .orElseThrow(
                  () ->
                      new CourseNotFoundException(
                          "Course not found with id: " + curriculumRequest.courseId()));

      Curriculum curriculum =
          Curriculum.builder()
              .course(course)
              .description(curriculumRequest.description())
              .modulesJSON(curriculumRequest.modulesJSON())
              .status(curriculumRequest.status())
              .build();

      return curriculumMapper.toResponseDto(curriculumRepository.save(curriculum));
    } catch (CurriculumNotCreatedException e) {
      log.error("Error occurred while creating curriculum: {}", e.getMessage());
      throw new CurriculumNotCreatedException("Failed to create curriculum");
    } catch (Exception e) {
      log.error("Unexpected error occurred while creating curriculum: {}", e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while creating the curriculum");
    }
  }

  /**
   * Retrieves all curricula available in the system.
   *
   * <p>Fetches every {@link Curriculum} entity from the repository and maps each one
   * to a {@link CurriculumResponse} DTO.
   *
   * @return a {@link List} of {@link CurriculumResponse} objects representing all curricula;
   *         returns an empty list if no curricula exist
   * @throws CurriculumsNotFoundException if a retrieval-specific error occurs
   * @throws InternalServerErrorException if an unexpected error occurs during retrieval
   */
  @Override
  public List<CurriculumResponse> getAllCurriculums() {
    try {
      return curriculumRepository.findAll().stream().map(curriculumMapper::toResponseDto).toList();
    } catch (CurriculumsNotFoundException e) {
      log.error("Error occurred while fetching curriculums: {}", e.getMessage());
      throw new CurriculumsNotFoundException("No curriculums found");
    } catch (Exception e) {
      log.error("Unexpected error occurred while fetching curriculums: {}", e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while fetching curriculums");
    }
  }

  /**
   * Retrieves a single curriculum by its unique identifier.
   *
   * @param id the {@link UUID} of the curriculum to retrieve
   * @return a {@link CurriculumResponse} representing the found curriculum
   * @throws CurriculumNotFoundException  if no curriculum exists with the given {@code id}
   * @throws InternalServerErrorException if an unexpected error occurs during retrieval
   */
  @Override
  public CurriculumResponse getCurriculumById(UUID id) {
    try {
      Curriculum curriculum =
          curriculumRepository
              .findById(id)
              .orElseThrow(
                  () -> new CurriculumNotFoundException("Curriculum not found with id: " + id));

      return curriculumMapper.toResponseDto(curriculum);
    } catch (CurriculumNotFoundException e) {
      log.error("Error occurred while fetching curriculum with ID {}: {}", id, e.getMessage());
      throw new CurriculumNotFoundException("Curriculum with id: " + id + " not found");
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while fetching curriculum with ID {}: {}", id, e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while fetching the curriculum");
    }
  }

  /**
   * Updates an existing curriculum identified by its unique identifier.
   *
   * <p>Only non-null fields in the {@code curriculumRequest} are applied to the existing
   * curriculum, allowing partial updates. If a new {@code courseId} is provided, the
   * referenced course is validated and reassigned.
   *
   * @param id                the {@link UUID} of the curriculum to update
   * @param curriculumRequest the request DTO containing the fields to update;
   *                          any null field is ignored
   * @throws CurriculumNotFoundException  if no curriculum exists with the given {@code id}
   * @throws CourseNotFoundException      if a new {@code courseId} is provided but the course does not exist
   * @throws CurriculumNotUpdatedException if the curriculum could not be updated due to a persistence error
   * @throws InternalServerErrorException if an unexpected error occurs during the update
   */
  @Override
  public void updateCurriculumById(UUID id, CurriculumRequest curriculumRequest) {
    try {
      Curriculum curriculum =
          curriculumRepository
              .findById(id)
              .orElseThrow(
                  () -> new CurriculumNotFoundException("Curriculum not found with id: " + id));

      if (curriculumRequest.courseId() != null) {
        Course course =
            courseRepository
                .findById(curriculumRequest.courseId())
                .orElseThrow(
                    () ->
                        new CourseNotFoundException(
                            "Course not found with id: " + curriculumRequest.courseId()));

        curriculum.setCourse(course);
      }

      if (curriculumRequest.description() != null) {
        curriculum.setDescription(curriculumRequest.description());
      }

      if (curriculumRequest.modulesJSON() != null) {
        curriculum.setModulesJSON(curriculumRequest.modulesJSON());
      }

      if (curriculumRequest.status() != null) {
        curriculum.setStatus(curriculumRequest.status());
      }
      curriculumRepository.save(curriculum);
    } catch (CurriculumNotUpdatedException e) {
      throw new CurriculumNotUpdatedException("Failed to update curriculum");
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while updating curriculum with ID {}: {}", id, e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while updating the curriculum");
    }
  }

  /**
   * Deletes the curriculum identified by the given unique identifier.
   *
   * <p>Verifies that the curriculum exists before invoking deletion. If the curriculum
   * is not found, a {@link CurriculumNotFoundException} is thrown immediately.
   *
   * @param id the {@link UUID} of the curriculum to delete
   * @throws CurriculumNotFoundException   if no curriculum exists with the given {@code id}
   * @throws CurriculumNotDeletedException if the curriculum could not be deleted due to a persistence error
   * @throws InternalServerErrorException  if an unexpected error occurs during deletion
   */
  @Override
  public void deleteCurriculumById(UUID id) {
    try {
      if (!curriculumRepository.existsById(id)) {
        throw new CurriculumNotFoundException("Curriculum not found with id: " + id);
      }

      curriculumRepository.deleteById(id);
    } catch (CurriculumNotDeletedException e) {
      throw new CurriculumNotDeletedException("Curriculum not found with id: " + id);
    } catch (Exception e) {
      log.error(
          "Unexpected error occurred while deleting curriculum with ID {}: {}", id, e.getMessage());
      throw new InternalServerErrorException(
          "An unexpected error occurred while deleting the curriculum");
    }
  }
}
