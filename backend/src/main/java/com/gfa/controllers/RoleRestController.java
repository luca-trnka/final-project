package com.gfa.controllers;

import com.gfa.dtos.ErrorResponseDto;
import com.gfa.dtos.RoleRequestDto;
import com.gfa.dtos.RoleResponseDto;
import com.gfa.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/roles")
public class RoleRestController {

    private final RoleService roleService;

    @Autowired
    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping("")
    public ResponseEntity<List<RoleResponseDto>> index() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
    @PostMapping("/")
    public ResponseEntity<RoleRequestDto> store(@RequestBody RoleRequestDto roleRequestDto) throws AuthenticationException {
        return ResponseEntity.status(201).body(roleService.storeRole(roleRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> show(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.findRole(id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponseDto> update(@PathVariable("id") Long id, @RequestBody RoleRequestDto roleRequestDto) throws AuthenticationException {
        return ResponseEntity.ok(roleService.updateRole(id, roleRequestDto.getRole()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(201).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentExceptionHandler(Exception e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> authenticationExceptionHandler(Exception e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> noSuchElementExceptionExceptionHandler(Exception e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(404).body(response);
    }

}

