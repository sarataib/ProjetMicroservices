package com.example.notification_service.consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmpruntEventConsumer {

    @KafkaListener(topics = "emprunt-created", groupId = "notification-group")
    public void consume(Object message) {
        System.out.println("NOTIFICATION REÇUE : " + message);
    }
}
