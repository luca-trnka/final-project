package com.gfa.services;

import com.gfa.dtos.ProjectRequestDto;
import com.gfa.dtos.ProjectResponseDto;
import com.gfa.models.Project;
import com.gfa.repositories.ProjectRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DatabaseProjectService.class})
@ExtendWith(SpringExtension.class)
class DatabaseProjectServiceTest {
    @Autowired
    private DatabaseProjectService databaseProjectService;

    @MockBean
    private ProjectRepository projectRepository;

    /**
     * Method under test: {@link DatabaseProjectService#getAllProjects()}
     */
    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(databaseProjectService.getAllProjects().isEmpty());
        verify(projectRepository).findAll();
    }

    /**
     * Method under test: {@link DatabaseProjectService#getAllProjects()}
     */
    @Test
    void testGetAllProjects2() {
        Project project = new Project();
        project.setName("Name");

        ArrayList<Project> projectList = new ArrayList<>();
        projectList.add(project);
        when(projectRepository.findAll()).thenReturn(projectList);
        List<ProjectResponseDto> actualAllProjects = databaseProjectService.getAllProjects();
        assertEquals(1, actualAllProjects.size());
        ProjectResponseDto getResult = actualAllProjects.get(0);
        assertNull(getResult.getDescription());
        assertEquals("Name", getResult.getName());
        assertNull(getResult.getInstances());
        assertNull(getResult.getId());
        verify(projectRepository).findAll();
    }

    /**
     * Method under test: {@link DatabaseProjectService#getAllProjects()}
     */
    @Test
    void testGetAllProjects3() {
        Project project = new Project();
        project.setName("Name");

        Project project2 = new Project();
        project2.setName("42");

        ArrayList<Project> projectList = new ArrayList<>();
        projectList.add(project2);
        projectList.add(project);
        when(projectRepository.findAll()).thenReturn(projectList);
        List<ProjectResponseDto> actualAllProjects = databaseProjectService.getAllProjects();
        assertEquals(2, actualAllProjects.size());
        ProjectResponseDto getResult = actualAllProjects.get(0);
        assertEquals("42", getResult.getName());
        ProjectResponseDto getResult2 = actualAllProjects.get(1);
        assertEquals("Name", getResult2.getName());
        assertNull(getResult2.getInstances());
        assertNull(getResult2.getId());
        assertNull(getResult2.getDescription());
        assertNull(getResult.getInstances());
        assertNull(getResult.getId());
        assertNull(getResult.getDescription());
        verify(projectRepository).findAll();
    }

    /**
     * Method under test: {@link DatabaseProjectService#getAllProjects()}
     */
    @Test
    void testGetAllProjects4() {
        when(projectRepository.findAll()).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService.getAllProjects());
        verify(projectRepository).findAll();
    }

    /**
     * Method under test: {@link DatabaseProjectService#addProject(ProjectRequestDto)}
     */
    @Test
    void testAddProject() throws AuthenticationException {
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthenticationException.class, () -> databaseProjectService
                .addProject(new ProjectRequestDto("Name", "The characteristics of someone or something", new ArrayList<>())));
        verify(projectRepository).existsByName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#addProject(ProjectRequestDto)}
     */
    @Test
    void testAddProject2() throws AuthenticationException {
        Project project = new Project();
        project.setName("Name");
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project);
        ProjectResponseDto actualAddProjectResult = databaseProjectService
                .addProject(new ProjectRequestDto("Name", "The characteristics of someone or something", new ArrayList<>()));
        assertEquals("The characteristics of someone or something", actualAddProjectResult.getDescription());
        assertEquals("Name", actualAddProjectResult.getName());
        assertTrue(actualAddProjectResult.getInstances().isEmpty());
        assertNull(actualAddProjectResult.getId());
        verify(projectRepository).existsByName(Mockito.<String>any());
        verify(projectRepository).save(Mockito.<Project>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#addProject(ProjectRequestDto)}
     */
    @Test
    void testAddProject3() throws AuthenticationException {
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService
                .addProject(new ProjectRequestDto(null, "The characteristics of someone or something", new ArrayList<>())));
    }

    /**
     * Method under test: {@link DatabaseProjectService#addProject(ProjectRequestDto)}
     */
    @Test
    void testAddProject4() throws AuthenticationException {
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService
                .addProject(new ProjectRequestDto("", "The characteristics of someone or something", new ArrayList<>())));
    }

    /**
     * Method under test: {@link DatabaseProjectService#addProject(ProjectRequestDto)}
     */
    @Test
    void testAddProject5() throws AuthenticationException {
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService.addProject(null));
    }

    /**
     * Method under test: {@link DatabaseProjectService#addProject(ProjectRequestDto)}
     */
    @Test
    void testAddProject6() throws AuthenticationException {
        when(projectRepository.existsByName(Mockito.<String>any())).thenThrow(new NoSuchElementException("foo"));
        assertThrows(NoSuchElementException.class, () -> databaseProjectService
                .addProject(new ProjectRequestDto("Name", "The characteristics of someone or something", new ArrayList<>())));
        verify(projectRepository).existsByName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#getProjectById(String)}
     */
    @Test
    void testGetProjectById() {
        Project project = new Project();
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        ProjectResponseDto actualProjectById = databaseProjectService.getProjectById("42");
        assertNull(actualProjectById.getDescription());
        assertEquals("Name", actualProjectById.getName());
        assertNull(actualProjectById.getInstances());
        assertNull(actualProjectById.getId());
        verify(projectRepository).existsById(Mockito.<Long>any());
        verify(projectRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#getProjectById(String)}
     */
    @Test
    void testGetProjectById2() {
        when(projectRepository.findById(Mockito.<Long>any())).thenThrow(new IllegalArgumentException("foo"));
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService.getProjectById("42"));
        verify(projectRepository).existsById(Mockito.<Long>any());
        verify(projectRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#getProjectById(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetProjectById3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.NoSuchElementException: No value present
        //       at java.util.Optional.get(Optional.java:135)
        //       at com.gfa.services.DatabaseProjectService.getProjectById(DatabaseProjectService.java:53)
        //   See https://diff.blue/R013 to resolve this issue.

        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        databaseProjectService.getProjectById("42");
    }

    /**
     * Method under test: {@link DatabaseProjectService#getProjectById(String)}
     */
    @Test
    void testGetProjectById4() {
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> databaseProjectService.getProjectById("42"));
        verify(projectRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#getProjectById(String)}
     */
    @Test
    void testGetProjectById5() {
        assertThrows(NumberFormatException.class, () -> databaseProjectService.getProjectById("Id"));
    }

    /**
     * Method under test: {@link DatabaseProjectService#updateProject(String, String)}
     */
    @Test
    void testUpdateProject() throws AuthenticationException {
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthenticationException.class, () -> databaseProjectService.updateProject("42", "Name"));
        verify(projectRepository).existsByName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#updateProject(String, String)}
     */
    @Test
    void testUpdateProject2() throws AuthenticationException {
        Project project = new Project();
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);

        Project project2 = new Project();
        project2.setName("Name");
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project2);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        ProjectResponseDto actualUpdateProjectResult = databaseProjectService.updateProject("42", "Name");
        assertNull(actualUpdateProjectResult.getDescription());
        assertEquals("Name", actualUpdateProjectResult.getName());
        assertNull(actualUpdateProjectResult.getInstances());
        assertNull(actualUpdateProjectResult.getId());
        verify(projectRepository).existsByName(Mockito.<String>any());
        verify(projectRepository).existsById(Mockito.<Long>any());
        verify(projectRepository).save(Mockito.<Project>any());
        verify(projectRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#updateProject(String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateProject3() throws AuthenticationException {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.gfa.services.DatabaseProjectService.updateProject(DatabaseProjectService.java:61)
        //   See https://diff.blue/R013 to resolve this issue.

        Project project = new Project();
        project.setName("Name");
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(null);
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        databaseProjectService.updateProject("42", "Name");
    }

    /**
     * Method under test: {@link DatabaseProjectService#updateProject(String, String)}
     */
    @Test
    void testUpdateProject4() throws AuthenticationException {
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> databaseProjectService.updateProject("42", "Name"));
        verify(projectRepository).existsByName(Mockito.<String>any());
        verify(projectRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#updateProject(String, String)}
     */
    @Test
    void testUpdateProject5() throws AuthenticationException {
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        assertThrows(NumberFormatException.class, () -> databaseProjectService.updateProject("Id", "Name"));
        verify(projectRepository).existsByName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#updateProject(String, String)}
     */
    @Test
    void testUpdateProject6() throws AuthenticationException {
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService.updateProject("42", null));
    }

    /**
     * Method under test: {@link DatabaseProjectService#updateProject(String, String)}
     */
    @Test
    void testUpdateProject7() throws AuthenticationException {
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService.updateProject("42", ""));
    }

    /**
     * Method under test: {@link DatabaseProjectService#deleteProjectById(String)}
     */
    @Test
    void testDeleteProjectById() {
        doNothing().when(projectRepository).deleteById(Mockito.<Long>any());
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        databaseProjectService.deleteProjectById("42");
        verify(projectRepository).existsById(Mockito.<Long>any());
        verify(projectRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#deleteProjectById(String)}
     */
    @Test
    void testDeleteProjectById2() {
        doThrow(new IllegalArgumentException("foo")).when(projectRepository).deleteById(Mockito.<Long>any());
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> databaseProjectService.deleteProjectById("42"));
        verify(projectRepository).existsById(Mockito.<Long>any());
        verify(projectRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#deleteProjectById(String)}
     */
    @Test
    void testDeleteProjectById3() {
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> databaseProjectService.deleteProjectById("42"));
        verify(projectRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DatabaseProjectService#deleteProjectById(String)}
     */
    @Test
    void testDeleteProjectById4() {
        assertThrows(NumberFormatException.class, () -> databaseProjectService.deleteProjectById("Id"));
    }
}