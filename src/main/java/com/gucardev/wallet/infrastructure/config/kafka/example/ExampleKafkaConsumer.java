package com.gucardev.wallet.infrastructure.config.kafka.example;

import com.gucardev.wallet.infrastructure.config.kafka.example.dto.ExampleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleKafkaConsumer {

    @KafkaListener(
            topics = "topic1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ExampleMessage message, Acknowledgment acknowledgment) {
        try {
            log.info("Processing message: {}", message);

            // Your business logic here
            log.info("Message content: {}", message.getContent());

            // Manually acknowledge the message after successful processing
            acknowledgment.acknowledge();
            log.debug("Message acknowledged successfully");

        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            // Don't acknowledge - message will be redelivered
        }
    }
}