package com.gucardev.wallet.infrastructure.config.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@Slf4j
public abstract class GenericKafkaConsumer<T> {

    private final Class<T> type;

    protected GenericKafkaConsumer(Class<T> type) {
        this.type = type;
    }

    /**
     * The Kafka listener method that processes incoming messages
     * Note: Spring EL expressions in KafkaListener annotation need bean references
     * that are properly recognized by Spring
     */
    @KafkaListener(
            topics = "#{@__listener.getTopic()}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "#{@__listener.getGroupId()}"
    )
    public void listen(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        String key = record.key();

        try {
            // Safely cast the incoming message to the expected type
            if (type.isInstance(record.value())) {
                T payload = type.cast(record.value());

                log.debug("Received message: topic={}, partition={}, offset={}, key={}",
                        record.topic(), record.partition(), record.offset(), key);

                processMessage(key, payload);
                ack.acknowledge();
                log.debug("Message processed successfully and acknowledged");
            } else {
                log.error("Received message of unexpected type. Expected: {}, Actual: {}",
                        type.getName(), record.value().getClass().getName());
                // Still acknowledge to avoid message getting stuck
                ack.acknowledge();
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            // Don't acknowledge - let the error handler retry
            throw e;
        }
    }

    /**
     * Abstract method to be implemented by concrete consumers
     */
    protected abstract void processMessage(String key, T payload);

    /**
     * Must be implemented to return the topic name
     */
    public abstract String getTopic();

    /**
     * Can be overridden to provide a custom group ID
     */
    public String getGroupId() {
        return "wallet-service";
    }
}