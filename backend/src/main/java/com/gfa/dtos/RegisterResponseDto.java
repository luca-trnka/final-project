package com.gfa.dtos;

public class RegisterResponseDto {
    private final String status;

    public RegisterResponseDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
