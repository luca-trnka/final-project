package com.gfa.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
public class RoleRequestDto {
    public final Long id;
    public final String role;

    @JsonCreator
    public RoleRequestDto(@JsonProperty("id") Long id, @JsonProperty("role") String role) {
        this.id = id;
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }
}
