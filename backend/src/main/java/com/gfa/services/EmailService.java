package com.gfa.services;

import com.gfa.dtos.RegisterResponseDto;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface EmailService {
    void sendVerificationEmail(String email, Long token) throws MessagingException;
    RegisterResponseDto verifyToken(Long token) throws MessagingException;
}
