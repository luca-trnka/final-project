package com.gfa.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProfileResponseDtoTest {
    @Test
    void test_createUserProfileResponseDto() {
        UserProfileResponseDto result = new UserProfileResponseDto( 1L,"name", "email");

        assertEquals(1L, result.getId());
        assertEquals("name", result.getName());
        assertEquals("email", result.getEmail());
    }
}
