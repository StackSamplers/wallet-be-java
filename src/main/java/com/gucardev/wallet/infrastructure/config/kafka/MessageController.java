package com.gucardev.wallet.infrastructure.config.kafka;

import com.gucardev.wallet.infrastructure.config.kafka.producer.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class MessageController {

    private final KafkaPublisher kafkaPublisher;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Map<String, Object> request) {
        String data = request.get("data").toString();
        kafkaPublisher.send(data);
        return ResponseEntity.ok("Message sent successfully to topic");
    }

}