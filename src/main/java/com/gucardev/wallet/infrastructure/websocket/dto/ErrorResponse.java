package com.gucardev.wallet.infrastructure.websocket.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String code = "GENERAL_ERROR";

    public ErrorResponse(String message) {
        this.message = StringUtils.abbreviate(message,"...",30);
    }

    public ErrorResponse(String message, String code) {
        this.message = StringUtils.abbreviate(message,"...",30);
        this.code = code;
    }
}