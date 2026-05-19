package com.terrarent.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendVerificationEmail(String recipientEmail, String verificationCode) {
        log.info("Sending verification email to {} with code: {}", recipientEmail, verificationCode);
        // In a real application, integrate with an email sending service (e.g., JavaMailSender, SendGrid, Mailgun)
        // For now, this is a mock implementation.
    }

    // Potentially other email methods like password reset, booking confirmation, etc.
}
