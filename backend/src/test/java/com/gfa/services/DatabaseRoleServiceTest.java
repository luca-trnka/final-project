package com.gfa.services;

import com.gfa.dtos.RoleRequestDto;
import com.gfa.dtos.RoleResponseDto;
import com.gfa.models.Role;
import com.gfa.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseRoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private DatabaseRoleService databaseRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_getAllRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("admin"));
        roles.add(new Role("supervisor"));
        when(roleRepository.findAll()).thenReturn(roles);

        List<RoleResponseDto> roleResponseDtos = databaseRoleService.getAllRoles();

        assertEquals(2, roleResponseDtos.size());
        assertEquals("admin", roleResponseDtos.get(0).getRole());
        assertEquals("supervisor", roleResponseDtos.get(1).getRole());
    }

    @Test
    void test_createRole_ok() {
        Role role = new Role("admin");
        when(roleRepository.save(role)).thenReturn(role);

        Role result = databaseRoleService.createRole(role);

        assertNotNull(result);
        assertEquals("admin", result.getName());
    }

    @Test
    void test_roleExists_returnTrue() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("admin"));
        roles.add(new Role("supervisor"));
        when(roleRepository.findAll()).thenReturn(roles);

        boolean result = databaseRoleService.roleExists("admin");

        assertTrue(result);
    }

    @Test
    void test_roleExists_returnFalse() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("admin"));
        roles.add(new Role("supervisor"));
        when(roleRepository.findAll()).thenReturn(roles);

        boolean result = databaseRoleService.roleExists("Moderator");

        assertFalse(result);
    }

    @Test
    void test_getRoleById() {
        Role role = new Role("admin");
        role.setId(1L);
        when(roleRepository.findRoleById(1L)).thenReturn(role);

        Role result = databaseRoleService.getRoleById(1L);

        assertNotNull(result);
        assertEquals("admin", result.getName());
    }

    @Test
    void test_storeRole_withValidRoleRequestDto() throws AuthenticationException {
        RoleRequestDto roleRequestDto = new RoleRequestDto(1L, "admin");
        Role role = new Role("admin");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleRequestDto result = databaseRoleService.storeRole(roleRequestDto);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals("admin", result.getRole());
    }

    @Test
    void test_findRole_withExistingRoleId() {
        Role role = new Role("admin");
        role.setId(1L);
        when(roleRepository.findRoleById(1L)).thenReturn(role);

        RoleResponseDto result = databaseRoleService.findRole(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("admin", result.getRole());
    }

    @Test
    void test_findRole_withNonExistingRoleId_throwNoSuchElementException() {
        when(roleRepository.findRoleById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> databaseRoleService.findRole(1L));
    }

    @Test
    void test_updateRole_withValidRoleIdAndName() {
        Role role = new Role("admin");
        role.setId(1L);
        when(roleRepository.findRoleById(1L)).thenReturn(role);

        RoleResponseDto result = databaseRoleService.updateRole(1L, "updatedAdmin");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("updatedAdmin", result.getRole());
    }

    @Test
    void test_updateRole_withInvalidName() {
        Role role = new Role("admin");
        when(roleRepository.findRoleById(1L)).thenReturn(role);

        assertThrows(IllegalArgumentException.class, () -> databaseRoleService.updateRole(1L, ""));
    }

    @Test
    void test_deleteRole_withExistingRoleId() {
        Role role = new Role("admin");
        when(roleRepository.findRoleById(1L)).thenReturn(role);

        databaseRoleService.deleteRole(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void test_deleteRole_withNonExistingRoleId() {
        when(roleRepository.findRoleById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> databaseRoleService.deleteRole(1L));
    }
}