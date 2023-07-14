package com.gfa.repositories;

import com.gfa.models.Role;
import com.gfa.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleRepositoryTest {
    private Role role = new Role("student");
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
}
    @Test
    void test_save_role() {

        Mockito.when(roleRepository.save(role)).thenReturn(role);

        Role savedRole = roleRepository.save(role);

        assertEquals(role, savedRole);
    }
    @Test
    public void test_retrieve_role() {

        role.setId(1L);
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        Role retrievedRole = roleRepository.findById(role.getId()).orElse(null);


        assertEquals(role.getName(), Objects.requireNonNull(retrievedRole).getName());
    }
}
