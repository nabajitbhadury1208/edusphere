package com.cts.edusphere.services.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;

import java.util.List;
import java.util.UUID;

public interface FacultyService {
    FacultyResponseDTO createFaculty(FacultyRequestDTO requestDTO);
    FacultyResponseDTO getFacultyById(UUID id);
    List<FacultyResponseDTO> getAllFaculties();
    List<FacultyResponseDTO> getFacultiesByDepartment(UUID departmentId);
    FacultyResponseDTO updateFaculty(UUID id, FacultyRequestDTO requestDTO);
    void deleteFaculty(UUID id);
}
