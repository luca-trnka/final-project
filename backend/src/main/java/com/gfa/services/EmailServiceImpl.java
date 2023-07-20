package com.gfa.services;

import com.gfa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
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
