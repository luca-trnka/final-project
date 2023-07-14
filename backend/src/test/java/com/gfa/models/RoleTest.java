package com.gfa.models;

import com.gfa.models.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class RoleTest {
    @Test
    void test_create_role() {
        Role role = new Role("student");
        assertEquals("student", role.getName());
    }

}

