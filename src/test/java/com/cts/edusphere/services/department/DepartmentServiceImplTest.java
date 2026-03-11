package com.cts.edusphere.services.department;

import com.cts.edusphere.common.dto.department.DepartmentRequestDTO;
import com.cts.edusphere.common.dto.department.DepartmentResponseDTO;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.DepartmentMapper;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.repositories.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentHeadRepository departmentHeadRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private UUID departmentId;
    private UUID headId;
    private Department department;
    private DepartmentHead head;
    private DepartmentRequestDTO departmentRequestDTO;
    private DepartmentResponseDTO departmentResponseDTO;

    @BeforeEach
    void setUp() {
        headId = UUID.fromString("123e4567-e89b-12d3-a456-426614174111");
        departmentId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");


        head = new DepartmentHead();
        head.setId(headId);
        head.setName("Prof. Smith");

        department = new Department();
        department.setId(departmentId);
        department.setDepartmentName("Computer Science");
        department.setDepartmentCode("CS");
        department.setContactInfo("cs@university.edu");
        department.setStatus(Status.ACTIVE);
        department.setHead(head);
        department.setCreatedAt(Instant.now());
        department.setUpdatedAt(Instant.now());

        departmentRequestDTO = new DepartmentRequestDTO(
                "Computer Science",
                "CS",
                "cs@university.edu",
                Status.ACTIVE,
                headId
        );

        departmentResponseDTO = new DepartmentResponseDTO(
                departmentId,
                "Computer Science",
                "CS",
                "cs@university.edu",
                Status.ACTIVE,
                headId,
                "Prof. Smith",
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    void testCreateDepartment_Success() {
        when(departmentMapper.toEntity(departmentRequestDTO)).thenReturn(department);
        when(departmentHeadRepository.findById(headId)).thenReturn(Optional.of(head));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponseDTO(department)).thenReturn(departmentResponseDTO);

        DepartmentResponseDTO result = departmentService.createDepartment(departmentRequestDTO);

        assertNotNull(result);
        assertEquals("Computer Science", result.departmentName());
        assertEquals("CS", result.departmentCode());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testCreateDepartment_WithoutHead() {
        DepartmentRequestDTO dtoWithoutHead = new DepartmentRequestDTO(
                "Computer Science",
                "CS",
                "cs@university.edu",
                Status.ACTIVE,
                null
        );
        when(departmentMapper.toEntity(dtoWithoutHead)).thenReturn(department);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponseDTO(department)).thenReturn(departmentResponseDTO);

        DepartmentResponseDTO result = departmentService.createDepartment(dtoWithoutHead);

        assertNotNull(result);
        verify(departmentHeadRepository, never()).findById(any());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testCreateDepartment_HeadNotFound() {
        when(departmentMapper.toEntity(departmentRequestDTO)).thenReturn(department);
        when(departmentHeadRepository.findById(headId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.createDepartment(departmentRequestDTO));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testGetDepartmentById_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentMapper.toResponseDTO(department)).thenReturn(departmentResponseDTO);

        DepartmentResponseDTO result = departmentService.getDepartmentById(departmentId);

        assertNotNull(result);
        assertEquals("Computer Science", result.departmentName());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartmentById(departmentId));
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testGetAllDepartments_Success() {
        Department dept2 = new Department();
        dept2.setId(UUID.randomUUID());
        dept2.setDepartmentName("Mathematics");

        List<Department> departments = List.of(department, dept2);

        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.toResponseDTO(department)).thenReturn(departmentResponseDTO);
        when(departmentMapper.toResponseDTO(dept2)).thenReturn(departmentResponseDTO);

        List<DepartmentResponseDTO> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDepartments_Empty() {
        when(departmentRepository.findAll()).thenReturn(List.of());

        List<DepartmentResponseDTO> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testUpdateDepartment_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentHeadRepository.findById(headId)).thenReturn(Optional.of(head));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponseDTO(department)).thenReturn(departmentResponseDTO);

        DepartmentResponseDTO result = departmentService.updateDepartment(departmentId, departmentRequestDTO);

        assertNotNull(result);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testUpdateDepartment_NotFound() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.updateDepartment(departmentId, departmentRequestDTO));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testPartiallyUpdateDepartment_Success() {
        DepartmentRequestDTO partialDTO = new DepartmentRequestDTO(
                "Updated Name",
                null,
                null,
                null,
                null
        );
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponseDTO(department)).thenReturn(departmentResponseDTO);

        DepartmentResponseDTO result = departmentService.updateDepartment(departmentId, partialDTO);

        assertNotNull(result);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testPartiallyUpdateDepartment_NotFound() {
        DepartmentRequestDTO partialDTO = new DepartmentRequestDTO(
                "Updated Name",
                null,
                null,
                null,
                null
        );
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.updateDepartment(departmentId, partialDTO));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testChangeDepartmentHead_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentHeadRepository.findById(headId)).thenReturn(Optional.of(head));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponseDTO(department)).thenReturn(departmentResponseDTO);

        DepartmentResponseDTO result = departmentService.changeDepartmentHead(departmentId, headId);

        assertNotNull(result);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentHeadRepository, times(1)).findById(headId);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testChangeDepartmentHead_DepartmentNotFound() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.changeDepartmentHead(departmentId, headId));
        verify(departmentHeadRepository, never()).findById(any());
    }

    @Test
    void testChangeDepartmentHead_HeadNotFound() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentHeadRepository.findById(headId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.changeDepartmentHead(departmentId, headId));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testDeleteDepartment_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        doNothing().when(departmentRepository).delete(department);

        departmentService.deleteDepartment(departmentId);

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    void testDeleteDepartment_NotFound() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.deleteDepartment(departmentId));
        verify(departmentRepository, never()).delete(any());
    }
}

