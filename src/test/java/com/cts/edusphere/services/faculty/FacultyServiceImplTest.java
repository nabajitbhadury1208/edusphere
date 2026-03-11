package com.cts.edusphere.services.faculty;

import com.cts.edusphere.common.dto.faculty.FacultyRequestDTO;
import com.cts.edusphere.common.dto.faculty.FacultyResponseDTO;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.FacultyMapper;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.Faculty;
import com.cts.edusphere.repositories.DepartmentRepository;
import com.cts.edusphere.repositories.FacultyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceImplTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private FacultyMapper facultyMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    private UUID facultyId;
    private UUID departmentId;
    private Faculty faculty;
    private Department department;
    private FacultyRequestDTO facultyRequestDTO;
    private FacultyResponseDTO facultyResponseDTO;

    @BeforeEach
    void setUp() {
        facultyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174111");
        departmentId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        department = new Department();
        department.setId(departmentId);
        department.setDepartmentName("Computer Science");
        department.setDepartmentCode("CS");
        department.setStatus(Status.ACTIVE);

        faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("John Doe");
        faculty.setEmail("john@example.com");
        faculty.setPhone("1234567890");
        faculty.setPassword("password123");
        faculty.setPosition("Associate Professor");
        faculty.setRoles(Set.of(Role.FACULTY));
        faculty.setStatus(Status.ACTIVE);
        faculty.setDepartment(department);
        faculty.setJoinDate(Instant.now());
        faculty.setCreatedAt(Instant.now());
        faculty.setUpdatedAt(Instant.now());

        facultyRequestDTO = new FacultyRequestDTO(
                "John Doe",
                "john@example.com",
                "1234567890",
                "password123",
                "Associate Professor",
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                Status.ACTIVE
        );

        facultyResponseDTO = new FacultyResponseDTO(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174111"),
                "John Doe",
                "john@example.com",
                "1234567890",
                Role.FACULTY,
                Status.ACTIVE,
                "Associate Professor",
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "Computer Science",
                Instant.now(),
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    void testCreateFaculty_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(facultyMapper.toEntity(facultyRequestDTO)).thenReturn(faculty);
        when(passwordEncoder.encode(faculty.getPassword())).thenReturn("password123");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyMapper.toResponseDTO(faculty)).thenReturn(facultyResponseDTO);

        FacultyResponseDTO result = facultyService.createFaculty(facultyRequestDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("john@example.com", result.email());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(facultyRepository, times(1)).save(any(Faculty.class));
        verify(facultyMapper, times(1)).toEntity(facultyRequestDTO);
    }

    @Test
    void testCreateFaculty_DepartmentNotFound() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.createFaculty(facultyRequestDTO));
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(facultyRepository, never()).save(any());
    }

    @Test
    void testGetFacultyById_Success() {
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(facultyMapper.toResponseDTO(faculty)).thenReturn(facultyResponseDTO);

        FacultyResponseDTO result = facultyService.getFacultyById(facultyId);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
        verify(facultyRepository, times(1)).findById(facultyId);
    }

    @Test
    void testGetFacultyById_NotFound() {
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.getFacultyById(facultyId));
        verify(facultyRepository, times(1)).findById(facultyId);
    }

    @Test
    void testGetAllFaculties_Success() {
        Faculty faculty2 = new Faculty();
        faculty2.setId(UUID.randomUUID());
        faculty2.setName("Jane Smith");

        List<Faculty> faculties = List.of(faculty, faculty2);
        List<FacultyResponseDTO> dtos = List.of(facultyResponseDTO);

        when(facultyRepository.findAll()).thenReturn(faculties);
        when(facultyMapper.toResponseDTO(faculty)).thenReturn(facultyResponseDTO);
        when(facultyMapper.toResponseDTO(faculty2)).thenReturn(facultyResponseDTO);

        List<FacultyResponseDTO> result = facultyService.getAllFaculties();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFaculties_Empty() {
        when(facultyRepository.findAll()).thenReturn(List.of());

        List<FacultyResponseDTO> result = facultyService.getAllFaculties();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void testGetFacultiesByDepartment_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        department.setFaculties(List.of(faculty));
        when(facultyMapper.toResponseDTO(faculty)).thenReturn(facultyResponseDTO);

        List<FacultyResponseDTO> result = facultyService.getFacultiesByDepartment(departmentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testGetFacultiesByDepartment_DepartmentNotFound() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.getFacultiesByDepartment(departmentId));
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testUpdateFaculty_Success() {
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(passwordEncoder.encode(facultyRequestDTO.password())).thenReturn("password123");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyMapper.toResponseDTO(faculty)).thenReturn(facultyResponseDTO);

        FacultyResponseDTO result = facultyService.updateFaculty(facultyId, facultyRequestDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
        verify(facultyRepository, times(1)).findById(facultyId);
        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }

    @Test
    void testUpdateFaculty_NotFound() {
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.updateFaculty(facultyId, facultyRequestDTO));
        verify(facultyRepository, times(1)).findById(facultyId);
        verify(facultyRepository, never()).save(any());
    }

    @Test
    void testPartiallyUpdateFaculty_Success() {
        FacultyRequestDTO partialDTO = new FacultyRequestDTO("Updated Name", null, null, null, null, null, null);
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyMapper.toResponseDTO(faculty)).thenReturn(facultyResponseDTO);

        FacultyResponseDTO result = facultyService.partiallyUpdateFaculty(facultyId, partialDTO);

        assertNotNull(result);
        verify(facultyRepository, times(1)).findById(facultyId);
        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }

    @Test
    void testPartiallyUpdateFaculty_NotFound() {
        FacultyRequestDTO partialDTO = new FacultyRequestDTO("Updated Name", null, null, null, null, null, null);
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.partiallyUpdateFaculty(facultyId, partialDTO));
        verify(facultyRepository, never()).save(any());
    }

    @Test
    void testDeleteFaculty_Success() {
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        doNothing().when(facultyRepository).delete(faculty);

        facultyService.deleteFaculty(facultyId);

        verify(facultyRepository, times(1)).findById(facultyId);
        verify(facultyRepository, times(1)).delete(faculty);
    }

    @Test
    void testDeleteFaculty_NotFound() {
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.deleteFaculty(facultyId));
        verify(facultyRepository, never()).delete(any());
    }
}

