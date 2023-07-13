package com.gfa.dtos;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDtoTest {
    @Test
    void test_createResponseDto() {
        UserResponseDto result = new UserResponseDto(1L, "username", "email", LocalDateTime.now().toString());

        assertEquals(1L, result.getId());
        assertEquals("username", result.getUsername());
        assertEquals("email", result.getEmail());
        assertNotNull(result.getVerified_at());
    }
}