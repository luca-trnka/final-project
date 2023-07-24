package com.gfa.dtos;

public class LoginResponseDto {
    private Object message;
    private Object data;


    public LoginResponseDto(Object message, Object data) {
        this.message = message;
        this.data = data;
    }

    public LoginResponseDto(Object message) {
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}