package com.gfa.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDtoTest {
    @Test
    void test_createUserRequestDto() {
        UserRequestDto result = new UserRequestDto("username", "email", "password");

        assertEquals("username", result.getUsername());
        assertEquals("email", result.getEmail());
        assertEquals("password", result.getPassword());
    }
}