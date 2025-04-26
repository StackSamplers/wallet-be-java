package com.gucardev.wallet.infrastructure.mail.dto;

public record HtmlEmailRequest(String subject, String to, String templateName) {
}
