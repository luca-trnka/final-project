package com.gfa.services;

import com.gfa.dtos.RegisterResponseDto;
import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EmailServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_verifyToken_success() throws MessagingException {
        Long token = 123456L;
        User user = new User();
        user.setVerified(false);
        user.setVerificationToken(token);
        user.setVerificationTokenExpiresAt(System.currentTimeMillis() / 1000 + 3600);

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));

        RegisterResponseDto response = emailService.verifyToken(token);

        assertEquals("ok", response.getStatus());
        assertEquals(true, user.getVerified());
        assertEquals(0L, user.getVerificationTokenExpiresAt());
    }

    @Test
    public void test_verifyToken_invalidToken() {
        Long token = 987654L;

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> emailService.verifyToken(token));

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    public void test_verifyToken_validToken_userAlreadyVerified() throws MessagingException {
        Long token = 123456L;
        User user = new User();
        user.setVerified(true);
        user.setVerificationToken(token);
        user.setVerificationTokenExpiresAt(System.currentTimeMillis() / 1000 + 3600);

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> emailService.verifyToken(token));

        assertEquals("Email already verified", exception.getMessage());
    }

    // token expired scenario tested in integration test -> EmailControllerTest
}
