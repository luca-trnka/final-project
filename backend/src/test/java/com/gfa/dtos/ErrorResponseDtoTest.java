package com.gfa.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDtoTest {
    @Test
    void test_createErrorResponseDto() {
        ErrorResponseDto result = new ErrorResponseDto("some error");

        assertEquals("some error", result.error);
    }
}