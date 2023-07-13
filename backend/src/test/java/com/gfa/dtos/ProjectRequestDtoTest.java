package com.gfa.dtos;

import com.gfa.models.Instance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProjectRequestDtoTest {
    /**
     * Methods under test:
     *
     * {@link ProjectRequestDto#ProjectRequestDto(String, String, List)}
     * {@link ProjectRequestDto#getDescription()}
     * {@link ProjectRequestDto#getInstances()}
     * {@link ProjectRequestDto#getName()}
     */
    @Test
    void testConstructor() {
        ArrayList<Instance> instances = new ArrayList<>();
        ProjectRequestDto actualProjectRequestDto = new ProjectRequestDto("Name",
                "The characteristics of someone or something", instances);

        assertEquals("The characteristics of someone or something", actualProjectRequestDto.getDescription());
        assertSame(instances, actualProjectRequestDto.getInstances());
        assertEquals("Name", actualProjectRequestDto.getName());
    }
}