package com.gucardev.wallet.infrastructure.config.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericKafkaPublisher<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public CompletableFuture<SendResult<String, T>> send(String topic, T data) {
        return send(topic, null, data, null, null);
    }

    public CompletableFuture<SendResult<String, T>> send(String topic, T data,
                                                         Consumer<SendResult<String, T>> successCallback,
                                                         Consumer<Throwable> errorCallback) {
        return send(topic, null, data, successCallback, errorCallback);
    }

    public CompletableFuture<SendResult<String, T>> send(String topic, String key, T data) {
        return send(topic, key, data, null, null);
    }

    public CompletableFuture<SendResult<String, T>> send(
            String topic,
            String key,
            T data,
            Consumer<SendResult<String, T>> successCallback,
            Consumer<Throwable> errorCallback) {

        log.debug("Sending message to topic: {}, key: {}, data: {}", topic, key, data);

        try {
            CompletableFuture<SendResult<String, T>> future = kafkaTemplate.send(topic, key, data);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Message sent successfully to topic: {}, partition: {}, offset: {}",
                            topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

                    if (successCallback != null) {
                        try {
                            successCallback.accept(result);
                        } catch (Exception e) {
                            log.error("Error in success callback for topic: {}", topic, e);
                        }
                    }
                } else {
                    log.error("Failed to send message to topic: {}", topic, ex);

                    if (errorCallback != null) {
                        try {
                            errorCallback.accept(ex);
                        } catch (Exception e) {
                            log.error("Error in error callback for topic: {}", topic, e);
                        }
                    }
                }
            });

            return future;
        } catch (Exception e) {
            log.error("Error while preparing to send message to topic: {}", topic, e);
            CompletableFuture<SendResult<String, T>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);

            if (errorCallback != null) {
                try {
                    errorCallback.accept(e);
                } catch (Exception callbackError) {
                    log.error("Error in error callback for topic: {}", topic, callbackError);
                }
            }

            return failedFuture;
        }
    }
}