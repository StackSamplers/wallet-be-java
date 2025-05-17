package com.gucardev.wallet.infrastructure.config.kafka.producer;

import com.gucardev.wallet.infrastructure.config.kafka.dto.ExampleMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPublisher {

    private final GenericKafkaPublisher<Object> kafkaPublisher;
    private static final String NOTIFICATION_TOPIC = "topic1";

    public void send(String data) {
//        kafkaPublisher.send(NOTIFICATION_TOPIC, data);
        kafkaPublisher.send(NOTIFICATION_TOPIC, new ExampleMessage(data),
                // Success callback
                result -> {
                    // Update transaction with PROCESSED status and Kafka metadata
                    log.info("Transaction processed successfully");
                },
                // Error callback
                error -> {
                    // Update transaction with FAILED status and error message
                    log.error("Transaction failed to process", error);
                });
    }

}
