package com.gfa.services;

import com.gfa.dtos.RoleRequestDto;
import com.gfa.dtos.RoleResponseDto;
import com.gfa.models.Role;
import com.gfa.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseRoleService implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public DatabaseRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(Role -> new RoleResponseDto(Role.getId(), Role.getName()))
                .collect(Collectors.toList());
    }
    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public boolean roleExists(String roleName) {
        return roleRepository.findAll().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }
    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findRoleById(id);
    }

    public RoleRequestDto storeRole(RoleRequestDto roleRequestDto) throws AuthenticationException {
        String role = roleRequestDto.getRole();
        checkRole(role);
        Role newRole = new Role(role);
        Role savedRole = roleRepository.save(newRole);
        return new RoleRequestDto(savedRole.getId(), savedRole.getName());
    }
    public RoleResponseDto findRole(Long id) {
        checkId(id);
        Role role = getRoleById(id);
        roleNotFoundCheck(role);
        return new RoleResponseDto(role.getId(), role.getName());
    }
    public RoleResponseDto updateRole(Long id, String name) {
        checkId(id);
        Role role = getRoleById(id);
        roleNotFoundCheck(role);

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid data");
        }
        role.setName(name);
        return new RoleResponseDto(role.getId(), role.getName());
    }

    public void deleteRole(Long id) {
        checkId(id);
        Role role = getRoleById(id);
        roleNotFoundCheck(role);
        roleRepository.deleteById(id);
    }

    private void checkRole(String role) throws AuthenticationException {
        if (role == null || role.isEmpty())
            throw new IllegalArgumentException("Role is required");

        if (roleExists(role))
            throw new AuthenticationException("Role already exists");
    }

    private void roleNotFoundCheck(Role role) {
        if (role == null) {
            throw new NoSuchElementException("Role not found");
        }
    }
    private void checkId(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }
    }
}


