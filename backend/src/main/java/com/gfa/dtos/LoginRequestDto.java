package com.gfa.dtos;

public class LoginRequestDto {
    private String username;
    private String password;

    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequestDto() {
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}