package com.gucardev.wallet.infrastructure.mail.dto;

public record EmailRequest(String to, String subject, String body) {
}
