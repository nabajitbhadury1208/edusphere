package com.cts.edusphere.services.research_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.cts.edusphere.common.dto.research_project.ResearchProjectRequest;
import com.cts.edusphere.common.dto.research_project.ResearchProjectResponse;
import com.cts.edusphere.enums.ProjectStatus;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.mappers.research_project.ResearchProjectMapper;
import com.cts.edusphere.modules.faculty.Faculty;
import com.cts.edusphere.modules.research_project.ResearchProject;
import com.cts.edusphere.modules.student.Student;
import com.cts.edusphere.repositories.faculty.FacultyRepository;
import com.cts.edusphere.repositories.research_project.ResearchProjectRepository;
import com.cts.edusphere.repositories.student.StudentRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResearchProjectServiceImplTest {

    @Mock
    private ResearchProjectRepository researchProjectRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ResearchProjectMapper researchProjectMapper;

    @InjectMocks
    private ResearchProjectServiceImpl researchProjectService;

    private UUID projectId;
    private UUID leadFacultyId;
    private UUID facultyMemberId;
    private UUID studentId;
    private Faculty leadFaculty;
    private Faculty facultyMember;
    private Student student;
    private ResearchProjectRequest request;
    private ResearchProject project;
    private ResearchProjectResponse response;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        leadFacultyId = UUID.randomUUID();
        facultyMemberId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        leadFaculty = Faculty.builder()
                .id(leadFacultyId)
                .name("Lead Faculty")
                .build();

        facultyMember = Faculty.builder()
                .id(facultyMemberId)
                .name("Co-Investigator")
                .build();

        student = Student.builder()
                .id(studentId)
                .name("Research Student")
                .build();

        request = new ResearchProjectRequest(
                "AI in Education",
                leadFacultyId,
                List.of(facultyMemberId),
                List.of(studentId),
                LocalDate.now(),
                LocalDate.now().plusMonths(6),
                ProjectStatus.ACTIVE
        );

        project = ResearchProject.builder()
                .id(projectId)
                .title("AI in Education")
                .facultyLead(leadFaculty)
                .associatedFacultyMembers(new ArrayList<>(List.of(facultyMember)))
                .participatedStudents(new ArrayList<>(List.of(student)))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(6))
                .status(ProjectStatus.ACTIVE)
                .build();

        response = new ResearchProjectResponse(
                "AI in Education",
                leadFacultyId,
                ProjectStatus.ACTIVE,
                project.getEndDate(),
                project.getStartDate(),
                List.of(facultyMemberId),
                List.of(studentId)
        );
    }

    @Test
    void createProject_Success() {
        when(facultyRepository.findById(leadFacultyId)).thenReturn(Optional.of(leadFaculty));
        when(facultyRepository.getReferenceById(facultyMemberId)).thenReturn(facultyMember);
        when(studentRepository.getReferenceById(studentId)).thenReturn(student);

        when(researchProjectMapper.toEntity(
                eq(request),
                eq(leadFaculty),
                anyList(),
                anyList()
        )).thenReturn(project);

        when(researchProjectRepository.save(any(ResearchProject.class))).thenReturn(project);
        when(researchProjectMapper.toResponse(project)).thenReturn(response);

        ResearchProjectResponse result = researchProjectService.createProject(request);

        assertNotNull(result);
        assertEquals("AI in Education", result.title());
        assertEquals(leadFacultyId, result.facultyId());
        assertEquals(ProjectStatus.ACTIVE, result.status());

        verify(facultyRepository).findById(leadFacultyId);
        verify(facultyRepository).getReferenceById(facultyMemberId);
        verify(studentRepository).getReferenceById(studentId);
        verify(researchProjectRepository).save(any(ResearchProject.class));
    }

    @Test
    void createProject_ThrowsException_WhenLeadFacultyNotFound() {
        when(facultyRepository.findById(leadFacultyId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> researchProjectService.createProject(request)
        );

        verify(researchProjectRepository, never()).save(any());
    }

    @Test
    void getAllProjects_ReturnsList() {
        when(researchProjectRepository.findAll()).thenReturn(List.of(project));
        when(researchProjectMapper.toResponse(project)).thenReturn(response);

        List<ResearchProjectResponse> results = researchProjectService.getAllProjects();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("AI in Education", results.get(0).title());
        verify(researchProjectRepository).findAll();
    }

    @Test
    void getAllProjects_ReturnsEmptyList() {
        when(researchProjectRepository.findAll()).thenReturn(List.of());

        List<ResearchProjectResponse> results = researchProjectService.getAllProjects();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(researchProjectRepository).findAll();
    }

    @Test
    void getProjectById_ReturnsResponse() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(researchProjectMapper.toResponse(project)).thenReturn(response);

        ResearchProjectResponse result = researchProjectService.getProjectById(projectId);

        assertNotNull(result);
        assertEquals(leadFacultyId, result.facultyId());
        verify(researchProjectRepository).findById(projectId);
    }

    @Test
    void getProjectById_ThrowsException_WhenNotFound() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> researchProjectService.getProjectById(projectId));
        verify(researchProjectRepository).findById(projectId);
    }

    @Test
    void addFacultyMember_Success() {
        project.setAssociatedFacultyMembers(new ArrayList<>());

        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(facultyRepository.findById(facultyMemberId)).thenReturn(Optional.of(facultyMember));
        when(researchProjectRepository.save(project)).thenReturn(project);
        when(researchProjectMapper.toResponse(project)).thenReturn(response);

        ResearchProjectResponse result = researchProjectService.addFacultyMember(projectId, facultyMemberId);

        assertNotNull(result);
        assertEquals(1, project.getAssociatedFacultyMembers().size());
        assertEquals(facultyMemberId, project.getAssociatedFacultyMembers().get(0).getId());
        verify(researchProjectRepository).save(project);
    }

    @Test
    void addFacultyMember_ThrowsException_WhenProjectNotFound() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> researchProjectService.addFacultyMember(projectId, facultyMemberId)
        );

        verify(facultyRepository, never()).findById(any());
        verify(researchProjectRepository, never()).save(any());
    }

    @Test
    void addFacultyMember_ThrowsException_WhenFacultyNotFound() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(facultyRepository.findById(facultyMemberId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> researchProjectService.addFacultyMember(projectId, facultyMemberId)
        );

        verify(researchProjectRepository, never()).save(any());
    }

    @Test
    void removeFacultyMember_Success() {
        project.setAssociatedFacultyMembers(new ArrayList<>(List.of(facultyMember)));

        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(researchProjectRepository.save(project)).thenReturn(project);
        when(researchProjectMapper.toResponse(project)).thenReturn(response);

        ResearchProjectResponse result = researchProjectService.removeFacultyMember(projectId, facultyMemberId);

        assertNotNull(result);
        assertTrue(project.getAssociatedFacultyMembers().isEmpty());
        verify(researchProjectRepository).save(project);
    }

    @Test
    void removeFacultyMember_ThrowsException_WhenProjectNotFound() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> researchProjectService.removeFacultyMember(projectId, facultyMemberId)
        );

        verify(researchProjectRepository, never()).save(any());
    }

    @Test
    void addStudent_Success() {
        project.setParticipatedStudents(new ArrayList<>());

        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(researchProjectRepository.save(project)).thenReturn(project);
        when(researchProjectMapper.toResponse(project)).thenReturn(response);

        ResearchProjectResponse result = researchProjectService.addStudent(projectId, studentId);

        assertNotNull(result);
        assertEquals(1, project.getParticipatedStudents().size());
        assertEquals(studentId, project.getParticipatedStudents().get(0).getId());
        verify(researchProjectRepository).save(project);
    }

    @Test
    void addStudent_ThrowsException_WhenProjectNotFound() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> researchProjectService.addStudent(projectId, studentId)
        );

        verify(studentRepository, never()).findById(any());
        verify(researchProjectRepository, never()).save(any());
    }

    @Test
    void addStudent_ThrowsException_WhenStudentNotFound() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> researchProjectService.addStudent(projectId, studentId)
        );

        verify(researchProjectRepository, never()).save(any());
    }

    @Test
    void removeStudent_Success() {
        project.setParticipatedStudents(new ArrayList<>(List.of(student)));

        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(researchProjectRepository.save(project)).thenReturn(project);
        when(researchProjectMapper.toResponse(project)).thenReturn(response);

        ResearchProjectResponse result = researchProjectService.removeStudent(projectId, studentId);

        assertNotNull(result);
        assertTrue(project.getParticipatedStudents().isEmpty());
        verify(researchProjectRepository).save(project);
    }

    @Test
    void removeStudent_ThrowsException_WhenProjectNotFound() {
        when(researchProjectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> researchProjectService.removeStudent(projectId, studentId)
        );

        verify(researchProjectRepository, never()).save(any());
    }

    @Test
    void deleteProject_Success() {
        when(researchProjectRepository.existsById(projectId)).thenReturn(true);

        researchProjectService.deleteProject(projectId);

        verify(researchProjectRepository).deleteById(projectId);
    }

    @Test
    void deleteProject_ThrowsException_WhenNotFound() {
        when(researchProjectRepository.existsById(projectId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> researchProjectService.deleteProject(projectId));
        verify(researchProjectRepository, never()).deleteById(projectId);
    }
}