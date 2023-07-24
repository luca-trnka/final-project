package com.gfa.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProfileRequestDtoTest {
    @Test
    void test_createUserProfileRequestDto() {
        UserProfileRequestDto result = new UserProfileRequestDto("name", "email", "password");

        assertEquals("name", result.getName());
        assertEquals("email", result.getEmail());
        assertEquals("password", result.getPassword());
    }
}
