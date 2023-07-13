package com.gfa.dtos;

import com.gfa.models.Instance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProjectResponseDtoTest {
    /**
     * Methods under test:
     *
     *   {@link ProjectResponseDto#ProjectResponseDto(Long, String, String, List)}
     *   {@link ProjectResponseDto#getDescription()}
     *   {@link ProjectResponseDto#getId()}
     *   {@link ProjectResponseDto#getInstances()}
     *   {@link ProjectResponseDto#getName()}
     */
    @Test
    void testConstructor() {
        ArrayList<Instance> instances = new ArrayList<>();
        ProjectResponseDto actualProjectResponseDto = new ProjectResponseDto(1L, "Name",
                "The characteristics of someone or something", instances);

        assertEquals("The characteristics of someone or something", actualProjectResponseDto.getDescription());
        assertEquals(1L, actualProjectResponseDto.getId().longValue());
        assertSame(instances, actualProjectResponseDto.getInstances());
        assertEquals("Name", actualProjectResponseDto.getName());
    }
}