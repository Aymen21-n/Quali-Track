package com.qualitrack.notification.service;

import com.qualitrack.notification.dto.NotificationEmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerEmail(NotificationEmailDto notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@quali-track.local");
        message.setTo(notification.getDestinataire());
        message.setSubject(notification.getObjet());
        message.setText(notification.getContenu());

        logger.info("Envoi d'un email à {}", notification.getDestinataire());
        mailSender.send(message);
    }
}

