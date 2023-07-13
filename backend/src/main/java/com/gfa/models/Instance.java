package com.gfa.models;

import javax.persistence.*;

@Entity
public class Instance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String region;
    private String operatingSystem;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    public Instance() {
    }

    public Instance(String name, String region, String operatingSystem, User user, Project project) {
        this.name = name;
        this.region = region;
        this.operatingSystem = operatingSystem;
        this.user = user;
        this.project = project;
    }

    public Instance(String name, String region, String operatingSystem) {
        this.name = name;
        this.region = region;
        this.operatingSystem = operatingSystem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
