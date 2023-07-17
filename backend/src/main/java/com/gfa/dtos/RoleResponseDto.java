package com.gfa.dtos;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoleResponseDto {
    public final Long id;
    public final String role;

    @JsonCreator
    public RoleResponseDto(@JsonProperty("id") Long id, @JsonProperty("role") String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}