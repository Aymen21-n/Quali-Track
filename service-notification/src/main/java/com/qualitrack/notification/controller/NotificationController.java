package com.qualitrack.notification.controller;

import com.qualitrack.notification.dto.NotificationEmailDto;
import com.qualitrack.notification.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public ResponseEntity<Void> envoyerEmail(@Valid @RequestBody NotificationEmailDto notification) {
        emailService.envoyerEmail(notification);
        return ResponseEntity.noContent().build();
    }
}

