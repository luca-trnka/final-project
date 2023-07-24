package com.gfa.controllers;

import com.gfa.dtos.*;
import com.gfa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;

import static org.springframework.http.HttpStatus.*;

@RestController
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody (required = false) UserRequestDto newUserDTO) throws AuthenticationException, MessagingException {
        return ResponseEntity.status(CREATED).body(userService.addUser(newUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequestDto loginDetails) {
        return ResponseEntity.status(OK).body(userService.login(loginDetails));
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<ErrorResponseDto> runtimeExceptionHandler(Exception e) {
        // handles IllegalArgumentException and NumberFormatException
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<ErrorResponseDto> authenticationExceptionHandler(Exception e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<ErrorResponseDto> badCredentialExceptionHandler(Exception e) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponseDto(e.getMessage()));
    }
}