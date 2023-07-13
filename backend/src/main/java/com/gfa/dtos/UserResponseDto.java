package com.gfa.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponseDto {
    private final Long id;
    private final String username;
    private final String email;
    @JsonProperty("verified_at")
    private final String verifiedAt;

    public UserResponseDto(Long id, String username, String email, String verifiedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.verifiedAt = verifiedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getVerified_at() {
        return verifiedAt;
    }
}
