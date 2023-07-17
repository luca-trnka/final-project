package com.gfa.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleRequestDtoTest {
    @Test
    void test_createRoleRequestDto() {
        RoleRequestDto roleRequestDto = new RoleRequestDto(1L, "admin");

        assertEquals(1L, roleRequestDto.getId());
        assertEquals("admin", roleRequestDto.getRole());
    }
}
