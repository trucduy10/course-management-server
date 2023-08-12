package com.aptech.coursemanagementserver.utils;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    // @Autowired
    // public EmailSender(JavaMailSender javaMailSender) {
    // this.javaMailSender = javaMailSender;
    // }

    public void sendEmail(String from, String displayName, String to, String subject, String content)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from, displayName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // true indicates that the content is HTML
        javaMailSender.send(message);
    }
}
