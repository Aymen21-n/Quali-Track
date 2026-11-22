package com.qualitrack.notification.messaging;

import com.qualitrack.notification.dto.NotificationEmailDto;
import com.qualitrack.notification.service.EmailService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NotificationListener {
    private final Validator validator;
    private final EmailService emailService;

    public NotificationListener(Validator validator, EmailService emailService) {
        this.validator = validator;
        this.emailService = emailService;
    }

    @RabbitListener(queues = "notifications.email")
    public void recevoir(NotificationEmailDto notification) {
        Set<ConstraintViolation<NotificationEmailDto>> violations = validator.validate(notification);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        emailService.envoyerEmail(notification);
    }
}

