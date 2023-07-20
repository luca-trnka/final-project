package com.gfa.services;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface EmailService {
    void sendVerificationEmail(String email, Long token) throws MessagingException;
}
