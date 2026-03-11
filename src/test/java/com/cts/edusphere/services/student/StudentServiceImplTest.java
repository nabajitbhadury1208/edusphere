package com.cts.edusphere.services.student;

import com.cts.edusphere.common.dto.student.StudentRequestDTO;
import com.cts.edusphere.common.dto.student.StudentResponseDTO;
import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.StudentMapper;
import com.cts.edusphere.modules.Student;
import com.cts.edusphere.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentServiceImpl studentService;

    private UUID studentId;
    private Student student;
    private StudentRequestDTO studentRequestDTO;
    private StudentResponseDTO studentResponseDTO;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();

        student = new Student();
        student.setId(studentId);
        student.setName("Alice Johnson");
        student.setEmail("alice@example.com");
        student.setPhone("9876543210");
        student.setPassword("password123");
        student.setDob(LocalDate.of(2000, 1, 15));
        student.setGender(Gender.FEMALE);
        student.setAddress("123 Main St");
        student.setRoles(java.util.Set.of(Role.STUDENT));
        student.setStatus(Status.ACTIVE);
        student.setEnrollmentDate(Instant.now());
        student.setCreatedAt(Instant.now());
        student.setUpdatedAt(Instant.now());

        studentRequestDTO = new StudentRequestDTO(
                "Alice Johnson",
                "alice@example.com",
                "9876543210",
                "password123",
                LocalDate.of(2000, 1, 15),
                Gender.FEMALE,
                "123 Main St",
                Status.ACTIVE
        );

        studentResponseDTO = new StudentResponseDTO(
                studentId,
                "Alice Johnson",
                "alice@example.com",
                "9876543210",
                java.util.Set.of(Role.STUDENT),
                Status.ACTIVE,
                LocalDate.of(2000, 1, 15),
                Gender.FEMALE,
                "123 Main St",
                Instant.now(),
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    void testCreateStudent_Success() {
        when(studentMapper.toEntity(studentRequestDTO)).thenReturn(student);
        when(passwordEncoder.encode(student.getPassword())).thenReturn("password123");
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.toResponseDTO(student)).thenReturn(studentResponseDTO);

        StudentResponseDTO result = studentService.createStudent(studentRequestDTO);

        assertNotNull(result);
        assertEquals("Alice Johnson", result.name());
        assertEquals("alice@example.com", result.email());
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(passwordEncoder, times(1)).encode(student.getPassword());
    }

    @Test
    void testGetStudentById_Success() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toResponseDTO(student)).thenReturn(studentResponseDTO);

        StudentResponseDTO result = studentService.getStudentById(studentId);

        assertNotNull(result);
        assertEquals("Alice Johnson", result.name());
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(studentId));
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testGetAllStudents_Success() {
        Student student2 = new Student();
        student2.setId(UUID.randomUUID());
        student2.setName("Bob Smith");

        List<Student> students = List.of(student, student2);

        when(studentRepository.findAll()).thenReturn(students);
        when(studentMapper.toResponseDTO(student)).thenReturn(studentResponseDTO);
        when(studentMapper.toResponseDTO(student2)).thenReturn(studentResponseDTO);

        List<StudentResponseDTO> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllStudents_Empty() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<StudentResponseDTO> result = studentService.getAllStudents();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testUpdateStudent_Success() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.toResponseDTO(student)).thenReturn(studentResponseDTO);

        StudentResponseDTO result = studentService.updateStudent(studentId, studentRequestDTO);

        assertNotNull(result);
        assertEquals("Alice Johnson", result.name());
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(studentId, studentRequestDTO));
        verify(studentRepository, never()).save(any());
    }


    @Test
    void testDeleteStudent_Success() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(studentId);

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void testDeleteStudent_NotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(studentId));
        verify(studentRepository, never()).delete(any());
    }
}

