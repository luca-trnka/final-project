package com.gfa.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProjectTest {

    @Test
    void testConstructor() {
        Project actualProject = new Project();
        actualProject.setName("Name");
        assertNull(actualProject.getDescription());
        assertNull(actualProject.getId());
        assertNull(actualProject.getInstances());
        assertEquals("Name", actualProject.getName());
    }

    @Test
    void testConstructor2() {
        Project actualProject = new Project("Name", "The characteristics of someone or something");
        actualProject.setName("Name");
        assertEquals("The characteristics of someone or something", actualProject.getDescription());
        assertNull(actualProject.getId());
        assertEquals("Name", actualProject.getName());
    }
}