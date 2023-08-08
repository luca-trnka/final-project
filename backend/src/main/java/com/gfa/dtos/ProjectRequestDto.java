package com.gfa.dtos;

import com.gfa.models.Instance;

import java.util.List;

public final class ProjectRequestDto {
    private final String name;
    private final String description;
    private final List<Instance> instances;

    public ProjectRequestDto(String name, String description, List<Instance> instances) {
        this.name = name;
        this.description = description;
        this.instances = instances;
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