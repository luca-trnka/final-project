package com.gfa.dtos;

import com.gfa.models.Project;
import com.gfa.models.User;

public final class InstanceDto {
    private String name;
    private String region;
    private String operatingSystem;
    private User user;
    private Project project;

    public InstanceDto(String name, String region, String operatingSystem, User user, Project project) {
        this.name = name;
        this.region = region;
        this.operatingSystem = operatingSystem;
        this.user = user;
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
