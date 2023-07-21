package com.gfa.controllers;

import com.gfa.dtos.ErrorResponseDto;
import com.gfa.dtos.RegisterResponseDto;
import com.gfa.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class EmailController {
    private static EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email/verify/{token}")
    public ResponseEntity<RegisterResponseDto> verifyToken(@PathVariable Long token) throws MessagingException {
        return ResponseEntity.status(200).body(emailService.verifyToken(token));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentExceptionHandler(Exception e){
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(400).body(response);
    }
}
