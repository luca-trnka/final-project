package com.gfa.services;

import com.gfa.dtos.RegisterResponseDto;
import com.gfa.dtos.TfRequestDto;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Service
public interface EmailService {
    void sendVerificationEmail(String email, Long token) throws MessagingException;
    RegisterResponseDto verifyToken(Long token) throws MessagingException;
    void sendConfirmationEmail(String email, List<String> ips, TfRequestDto dto) throws MessagingException;
}
