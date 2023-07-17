package com.gfa.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleResponseDtoTest {
    @Test
    void test_createRoleResponseDto() {
        RoleResponseDto roleResponseDto = new RoleResponseDto(1L, "admin");

        assertEquals(1L, roleResponseDto.getId());
        assertEquals("admin", roleResponseDto.getRole());
    }
}