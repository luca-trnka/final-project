package com.gfa.services;

import com.gfa.dtos.ProjectRequestDto;
import com.gfa.dtos.ProjectResponseDto;

import javax.naming.AuthenticationException;
import java.util.List;

public interface ProjectService {
    List<ProjectResponseDto> getAllProjects();

    ProjectResponseDto addProject(ProjectRequestDto projectRequestDto) throws AuthenticationException;

    ProjectResponseDto getProjectById(String id);

    ProjectResponseDto updateProject(String id, String name) throws AuthenticationException;

    void deleteProjectById(String id);
}