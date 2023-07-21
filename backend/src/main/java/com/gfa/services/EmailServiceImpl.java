package com.gfa.services;

import com.gfa.dtos.RegisterResponseDto;
import com.gfa.models.User;
import com.gfa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public RegisterResponseDto verifyToken(Long token) throws MessagingException {
        if (!userRepository.findByVerificationToken(token).isPresent()) {
            throw new IllegalArgumentException("Invalid token");
        }
        User user = userRepository.findByVerificationToken(token).get();
        if (user.getVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }
        Long now = System.currentTimeMillis() / 1000;
        if (user.getVerificationTokenExpiresAt() < now) {
            SecureRandom secureRandom = new SecureRandom();
            Long newToken = secureRandom.nextLong();
            user.setVerificationToken(newToken);
            user.setVerificationTokenExpiresAt((System.currentTimeMillis() / 1000) + 3600);
            userRepository.save(user);

            sendVerificationEmail(user.getEmail(), newToken);

            throw new IllegalArgumentException("Token expired!");
        }

        user.setVerified(true);
        user.setVerifiedAt(System.currentTimeMillis() / 1000);
        user.setVerificationTokenExpiresAt(0L);

        userRepository.save(user);

        return new RegisterResponseDto("ok");
    }

    public void sendVerificationEmail(String email, Long token) throws MessagingException {
        String link = "http://localhost:8080/email/verify/" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("info@gfa.com");
        helper.setTo(email);
        helper.setSubject("confirm your email");
        helper.setText(buildVerificationEmail(link), true);

        mailSender.send(message);
    }

    private String buildVerificationEmail(String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "    <p>Hi,</p>\n" +
                "    <p>thank you for registering!</p>\n" +
                "    <p></p>Please click on the link below to activate your account:</p>\n" +
                "    <a href=" + link + ">activate NOW</a>\n" +
                "    <p>Link will expire in 60 minutes.</p>\n" +
                "    <p>See ya!</p>\n" +
                "</div>";
    }
}
