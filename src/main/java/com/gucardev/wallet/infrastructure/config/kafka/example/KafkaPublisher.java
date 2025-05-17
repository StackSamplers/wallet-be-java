package com.gucardev.wallet.infrastructure.config.kafka.example;

import com.gucardev.wallet.infrastructure.config.kafka.example.dto.ExampleMessage;
import com.gucardev.wallet.infrastructure.config.kafka.producer.GenericKafkaPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPublisher {

//    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final GenericKafkaPublisher<Object> kafkaPublisher;
    private static final String NOTIFICATION_TOPIC = "topic1";

    public void send(String data) {
//        kafkaTemplate.send(NOTIFICATION_TOPIC, data);
//        kafkaPublisher.send(NOTIFICATION_TOPIC, data);
        kafkaPublisher.send(NOTIFICATION_TOPIC, new ExampleMessage(data),
                // Success callback
                result -> {
                    log.info("Transaction processed successfully");
                },
                // Error callback
                error -> {
                    log.error("Transaction failed to process", error);
                });
    }

}
