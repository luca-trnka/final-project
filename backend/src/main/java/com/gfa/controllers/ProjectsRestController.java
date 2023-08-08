package com.gfa.controllers;

import com.gfa.dtos.ErrorResponseDto;
import com.gfa.dtos.ProjectRequestDto;
import com.gfa.dtos.ProjectResponseDto;
import com.gfa.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("projects")
public class ProjectsRestController {
    private final ProjectService projectService;

    @Autowired
    public ProjectsRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProjectResponseDto>> index() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PostMapping("/")
    public ResponseEntity<ProjectResponseDto> store(@RequestBody(required = false) ProjectRequestDto dto) throws AuthenticationException {
        return ResponseEntity.status(CREATED).body(projectService.addProject(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> show(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> update(@PathVariable String id, @RequestBody ProjectRequestDto dto) throws AuthenticationException {
        return ResponseEntity.ok(projectService.updateProject(id, dto.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable String id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<ErrorResponseDto> runtimeExceptionHandler(Exception e) {
        // handles IllegalArgumentException and NumberFormatException
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<ErrorResponseDto> authenticationExceptionHandler(Exception e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<ErrorResponseDto> noSuchElementExceptionExceptionHandler(Exception e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponseDto(e.getMessage()));
    }
}