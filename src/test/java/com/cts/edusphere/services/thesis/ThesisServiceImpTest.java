package com.cts.edusphere.services.thesis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.thesis.ThesisRequestDto;
import com.cts.edusphere.common.dto.thesis.ThesisResponseDto;
import com.cts.edusphere.enums.ThesisStatus;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.thesis.ThesisMapper;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.modules.thesis.Thesis;
import com.cts.edusphere.repositories.faculty.FacultyRepository;
import com.cts.edusphere.repositories.student.StudentRepository;
import com.cts.edusphere.repositories.thesis.ThesisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ThesisServiceImplTest {

    @Mock
    private ThesisRepository thesisRepository;
    @Mock
    private ThesisMapper thesisMapper;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private ThesisServiceImpl thesisService;

    private UUID thesisId;
    private UUID studentId;
    private UUID supervisorId;
    private Thesis thesis;
    private ThesisRequestDto requestDto;
    private ThesisResponseDto responseDto;

    @BeforeEach
    void setUp() {
        thesisId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        supervisorId = UUID.randomUUID();

        requestDto = new ThesisRequestDto(
                studentId,
                "AI in Modern Education",
                supervisorId,
                LocalDate.now().plusMonths(6),
                ThesisStatus.SUBMITTED
        );

        thesis = Thesis.builder()
                .student(Student.builder().id(studentId).build())
                .supervisor(Faculty.builder().id(supervisorId).build())
                .title("AI in Modern Education")
                .status(ThesisStatus.SUBMITTED)
                .submissionDate(LocalDate.now().plusMonths(6))
                .build();

        responseDto = new ThesisResponseDto(
                studentId,
                "AI in Modern Education",
                supervisorId,
                LocalDate.now().plusMonths(6),
                ThesisStatus.SUBMITTED
        );
    }

    // --- Create Thesis Tests ---

    @Test
    void createThesis_ShouldReturnResponse_WhenSuccessful() {
        when(thesisMapper.toEntity(any(ThesisRequestDto.class))).thenReturn(thesis);
        when(studentRepository.getReferenceById(studentId)).thenReturn(thesis.getStudent());
        when(facultyRepository.getReferenceById(supervisorId)).thenReturn(thesis.getSupervisor());
        when(thesisRepository.save(any(Thesis.class))).thenReturn(thesis);
        when(thesisMapper.toResponse(any(Thesis.class))).thenReturn(responseDto);

        ThesisResponseDto result = thesisService.createThesis(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.title(), result.title());
        verify(thesisRepository, times(1)).save(any(Thesis.class));
    }

    @Test
    void createThesis_ShouldThrowInternalServerError_OnException() {
        when(thesisMapper.toEntity(any())).thenThrow(new RuntimeException("Database down"));

        assertThrows(InternalServerErrorException.class, () -> thesisService.createThesis(requestDto));
    }

    // --- Get Thesis Tests ---

    @Test
    void getThesisById_ShouldReturnDto_WhenExists() {
        when(thesisRepository.findById(thesisId)).thenReturn(Optional.of(thesis));
        when(thesisMapper.toResponse(thesis)).thenReturn(responseDto);

        ThesisResponseDto result = thesisService.getThesisById(thesisId);

        assertNotNull(result);
        verify(thesisRepository).findById(thesisId);
    }

    @Test
    void getThesisById_ShouldThrowResourceNotFound_WhenNotExists() {
        when(thesisRepository.findById(thesisId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> thesisService.getThesisById(thesisId));
    }

    @Test
    void getThesisByStudent_ShouldReturnList() {
        when(thesisRepository.findByStudentId(studentId)).thenReturn(List.of(thesis));
        when(thesisMapper.toResponse(any())).thenReturn(responseDto);

        List<ThesisResponseDto> result = thesisService.getThesisByStudent(studentId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    // --- Update Thesis Tests ---

    @Test
    void updateThesis_ShouldUpdateAndReturnDto_WhenExists() {
        when(thesisRepository.findById(thesisId)).thenReturn(Optional.of(thesis));
        when(thesisRepository.save(any(Thesis.class))).thenReturn(thesis);
        when(thesisMapper.toResponse(any(Thesis.class))).thenReturn(responseDto);

        ThesisResponseDto result = thesisService.updateThesis(thesisId, requestDto);

        assertNotNull(result);
        verify(thesisRepository).save(any(Thesis.class));
    }

    @Test
    void updateThesis_ShouldThrowResourceNotFound_WhenNotFound() {
        when(thesisRepository.findById(thesisId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> thesisService.updateThesis(thesisId, requestDto));
    }

    // --- Delete Thesis Tests ---

    @Test
    void deleteThesis_ShouldSucceed_WhenExists() {
        when(thesisRepository.existsById(thesisId)).thenReturn(true);
        doNothing().when(thesisRepository).deleteById(thesisId);

        assertDoesNotThrow(() -> thesisService.deleteThesis(thesisId));
        verify(thesisRepository).deleteById(thesisId);
    }

    @Test
    void deleteThesis_ShouldThrowResourceNotFound_WhenNotExists() {
        when(thesisRepository.existsById(thesisId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> thesisService.deleteThesis(thesisId));
        verify(thesisRepository, never()).deleteById(any());
    }
}
