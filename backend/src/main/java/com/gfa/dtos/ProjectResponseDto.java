package com.gfa.dtos;

import com.gfa.models.Instance;

import java.util.List;

public final class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<Instance> instances;

    public ProjectResponseDto(Long id, String name, String description, List<Instance> instances) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instances = instances;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Instance> getInstances() {
        return instances;
    }
}