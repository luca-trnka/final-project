package com.gfa.services;

import com.gfa.dtos.ProjectRequestDto;
import com.gfa.dtos.ProjectResponseDto;
import com.gfa.models.Project;
import com.gfa.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DatabaseProjectService implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public DatabaseProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectResponseDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponseDto> responses = new ArrayList<>(projects.size());
        projects.forEach(project -> responses.add(new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getInstances()
        )));
        return responses;
    }

    @Override
    public ProjectResponseDto addProject(ProjectRequestDto projectRequestDto) throws AuthenticationException {
        if (projectRequestDto == null)
            throw new IllegalArgumentException("Request body is empty");

        verifyName(projectRequestDto.getName());
        Project project = new Project(projectRequestDto.getName(), projectRequestDto.getDescription());
        projectRepository.save(project);

        return new ProjectResponseDto(project.getId(), project.getName(), project.getDescription(), project.getInstances());
    }

    @Override
    public ProjectResponseDto getProjectById(String id) {
        Project project = projectRepository.findById(verifyId(id)).get();
        return new ProjectResponseDto(project.getId(), project.getName(), project.getDescription(), project.getInstances());
    }

    @Override
    public ProjectResponseDto updateProject(String id, String name) throws AuthenticationException {
        verifyName(name);

        Project project = projectRepository.findById(verifyId(id)).get();
        project.setName(name);
        projectRepository.save(project);

        return new ProjectResponseDto(project.getId(), project.getName(), project.getDescription(), project.getInstances());
    }

    @Override
    public void deleteProjectById(String id) {
        projectRepository.deleteById(verifyId(id));
    }

    private long verifyId(String id) throws NoSuchElementException {
        long confirmedId;
        try {
            confirmedId = Long.parseLong(id);
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("Invalid id");
        }

        if (confirmedId < 0)
            throw new NumberFormatException("Invalid id");

        if (!projectRepository.existsById(confirmedId))
            throw new NoSuchElementException("Project not found");

        return confirmedId;
    }

    private void verifyName(String name) throws AuthenticationException {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name is required");

        if (projectRepository.existsByName(name))
            throw new AuthenticationException("Name already exists");
    }
}