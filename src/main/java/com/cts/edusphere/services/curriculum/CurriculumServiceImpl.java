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

@Service
@Slf4j
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {
  private final CurriculumRepository curriculumRepository;
  private final CourseRepository courseRepository;
  private final CurriculumMapper curriculumMapper;

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
