package com.qualitrack.production.messaging;

import com.qualitrack.production.dto.NotificationEmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publier(NotificationEmailDto dto) {
        rabbitTemplate.convertAndSend("notifications.email", dto);
    }
}

