package com.gfa.services;

import com.gfa.dtos.RoleRequestDto;
import com.gfa.dtos.RoleResponseDto;
import com.gfa.models.Role;

import javax.naming.AuthenticationException;
import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    Role createRole(Role role);
    boolean roleExists(String roleName);
    Role getRoleById(Long id);
    RoleRequestDto storeRole(RoleRequestDto roleRequestDto) throws AuthenticationException;
    RoleResponseDto findRole(Long id);
    RoleResponseDto updateRole(Long id, String name);
    void deleteRole(Long id);
}
