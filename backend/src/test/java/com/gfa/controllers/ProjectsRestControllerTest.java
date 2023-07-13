package com.gfa.controllers;

import com.gfa.dtos.ProjectRequestDto;
import com.gfa.dtos.ProjectResponseDto;
import com.gfa.models.Project;
import com.gfa.repositories.ProjectRepository;
import com.gfa.services.DatabaseProjectService;
import com.gfa.services.ProjectService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ProjectsRestController.class})
@ExtendWith(SpringExtension.class)
class ProjectsRestControllerTest {
    @MockBean
    private ProjectService projectService;

    @Autowired
    private ProjectsRestController projectsRestController;

    @Test
    @Disabled("TODO: Complete this test")
    void testStore() throws AuthenticationException {
        Project project = new Project();
        project.setName("Name");
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(true);
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project);
        ProjectsRestController projectsRestController = new ProjectsRestController(
                new DatabaseProjectService(projectRepository));
        projectsRestController
                .store(new ProjectRequestDto("Name", "The characteristics of someone or something", new ArrayList<>()));
    }

    @Test
    void testStore2() throws AuthenticationException {
        Project project = new Project();
        project.setName("Name");
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project);
        ProjectsRestController projectsRestController = new ProjectsRestController(
                new DatabaseProjectService(projectRepository));
        ResponseEntity<ProjectResponseDto> actualStoreResult = projectsRestController
                .store(new ProjectRequestDto("Name", "The characteristics of someone or something", new ArrayList<>()));
        assertTrue(actualStoreResult.hasBody());
        assertTrue(actualStoreResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.CREATED, actualStoreResult.getStatusCode());
        ProjectResponseDto body = actualStoreResult.getBody();
        assertEquals("The characteristics of someone or something", body.getDescription());
        assertEquals("Name", body.getName());
        assertNull(body.getId());
        assertTrue(body.getInstances().isEmpty());
        verify(projectRepository).existsByName(Mockito.<String>any());
        verify(projectRepository).save(Mockito.<Project>any());
    }

    @Test
    @Disabled("TODO: Complete this test")
    void testUpdate() throws AuthenticationException {
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(true);
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        ProjectsRestController projectsRestController = new ProjectsRestController(
                new DatabaseProjectService(projectRepository));
        projectsRestController.update("42",
                new ProjectRequestDto("Name", "The characteristics of someone or something", new ArrayList<>()));
    }

    @Test
    void testUpdate2() throws AuthenticationException {
        Project project = new Project();
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);

        Project project2 = new Project();
        project2.setName("Name");
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project2);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        ProjectsRestController projectsRestController = new ProjectsRestController(
                new DatabaseProjectService(projectRepository));
        ResponseEntity<ProjectResponseDto> actualUpdateResult = projectsRestController.update("42",
                new ProjectRequestDto("Name", "The characteristics of someone or something", new ArrayList<>()));
        assertTrue(actualUpdateResult.hasBody());
        assertTrue(actualUpdateResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualUpdateResult.getStatusCode());
        ProjectResponseDto body = actualUpdateResult.getBody();
        assertNull(body.getDescription());
        assertEquals("Name", body.getName());
        assertNull(body.getId());
        assertNull(body.getInstances());
        verify(projectRepository).existsByName(Mockito.<String>any());
        verify(projectRepository).existsById(Mockito.<Long>any());
        verify(projectRepository).save(Mockito.<Project>any());
        verify(projectRepository).findById(Mockito.<Long>any());
    }

    @Test
    @Disabled("TODO: Complete this test")
    void testUpdate3() throws AuthenticationException {
        Project project = new Project();
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);

        Project project2 = new Project();
        project2.setName("Name");
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project2);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(projectRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(projectRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        (new ProjectsRestController(new DatabaseProjectService(projectRepository))).update("42", null);
    }

    @Test
    void testDestroy() throws Exception {
        doNothing().when(projectService).deleteProjectById(Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/projects/{id}", "42");
        MockMvcBuilders.standaloneSetup(projectsRestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDestroy2() throws Exception {
        doNothing().when(projectService).deleteProjectById(Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/projects/{id}", "42");
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(projectsRestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testIndex() throws Exception {
        when(projectService.getAllProjects()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects");
        MockMvcBuilders.standaloneSetup(projectsRestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testIndex2() throws Exception {
        when(projectService.getAllProjects()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects");
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(projectsRestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testShow() throws Exception {
        when(projectService.getProjectById(Mockito.<String>any())).thenReturn(
                new ProjectResponseDto(1L, "Name", "The characteristics of someone or something", new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects/{id}", "42");
        MockMvcBuilders.standaloneSetup(projectsRestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"instances\":[]}"));
    }
}