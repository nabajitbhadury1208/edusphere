package com.cts.edusphere.services.faculty;

import com.cts.edusphere.common.dto.Faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.Faculty.FacultyResponseDTO;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.FacultyMapper; // Updated Import
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.repositories.DepartmentRepository;
import com.cts.edusphere.repositories.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyMapper facultyMapper; // Injecting specific mapper
    private final PasswordEncoder passwordEncoder;

    @Override
    public FacultyResponseDTO createFaculty(FacultyRequestDTO requestDTO) {
        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + requestDTO.departmentId()));

        Faculty faculty = facultyMapper.toEntity(requestDTO);
        faculty.setRole(Role.FACULTY);
        faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
        faculty.setDepartment(department);

        Faculty savedFaculty = facultyRepository.save(faculty);
        return facultyMapper.toResponseDTO(savedFaculty);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyResponseDTO getFacultyById(UUID id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));
        return facultyMapper.toResponseDTO(faculty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDTO> getAllFaculties() {
        return facultyRepository.findAll().stream()
                .map(facultyMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDTO> getFacultiesByDepartment(UUID departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));

        return department.getFaculties().stream()
                .map(facultyMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FacultyResponseDTO updateFaculty(UUID id, FacultyRequestDTO requestDTO) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));

        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + requestDTO.departmentId()));

        faculty.setName(requestDTO.name());
        faculty.setEmail(requestDTO.email());
        faculty.setPhone(requestDTO.phone());
        faculty.setPassword(passwordEncoder.encode(requestDTO.password()));
        faculty.setPosition(requestDTO.position());
        faculty.setStatus(requestDTO.status());
        faculty.setDepartment(department);

        Faculty updatedFaculty = facultyRepository.save(faculty);
        return facultyMapper.toResponseDTO(updatedFaculty);
    }

    @Override
    public FacultyResponseDTO partiallyUpdateFaculty(UUID id, FacultyRequestDTO requestDTO) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));

        if (requestDTO.name() != null) {
            faculty.setName(requestDTO.name());
        }
        if (requestDTO.email() != null) {
            faculty.setEmail(requestDTO.email());
        }
        if (requestDTO.phone() != null) {
            faculty.setPhone(requestDTO.phone());
        }
        if (requestDTO.password() != null) {
            faculty.setPassword(passwordEncoder.encode(requestDTO.password()));
        }
        if (requestDTO.position() != null) {
            faculty.setPosition(requestDTO.position());
        }
        if (requestDTO.status() != null) {
            faculty.setStatus(requestDTO.status());
        }
        if (requestDTO.departmentId() != null) {
            Department department = departmentRepository.findById(requestDTO.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + requestDTO.departmentId()));
            faculty.setDepartment(department);
        }

        Faculty updatedFaculty = facultyRepository.save(faculty);
        return facultyMapper.toResponseDTO(updatedFaculty);
    }

    @Override
    public void deleteFaculty(UUID id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));
        facultyRepository.delete(faculty);
    }
}