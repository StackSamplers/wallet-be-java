package com.gucardev.wallet.infrastructure.config.kafka.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleMessage {
    private String content;
}