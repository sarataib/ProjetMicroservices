package com.org.emprunt.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.org.emprunt.kafka.EmpruntEvent;

@Service
public class EmpruntKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EmpruntKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmpruntEvent(Long empruntId, Long userId, Long bookId) {

        EmpruntEvent event =
                new EmpruntEvent(empruntId, userId, bookId);

        kafkaTemplate.send("emprunt-created", event);

        System.out.println("✅ Message Kafka envoyé : " + event);
    }
}
