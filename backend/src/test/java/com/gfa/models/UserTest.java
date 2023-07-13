package com.gfa.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void test_createUserEntity() {
        User user = new User("username", "email", "password");

        assertEquals("username", user.getUsername());
        assertEquals("email", user.getEmail());
        assertEquals("password", user.getPassword());
    }
}