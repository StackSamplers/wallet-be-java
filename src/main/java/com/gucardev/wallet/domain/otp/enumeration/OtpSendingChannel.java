package com.gucardev.wallet.domain.otp.enumeration;

import com.gucardev.wallet.domain.otp.service.EmailOtpSendUseCase;
import com.gucardev.wallet.domain.otp.service.base.OtpSendingService;
import com.gucardev.wallet.domain.otp.service.SmsOtpSendUseCase;
import com.gucardev.wallet.infrastructure.config.SpringContext;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OtpSendingChannel {
    EMAIL(EmailOtpSendUseCase.class), SMS(SmsOtpSendUseCase.class);

    private final Class<? extends OtpSendingService> service;

    OtpSendingChannel(Class<? extends OtpSendingService> notificationServiceClass) {
        this.service = notificationServiceClass;
    }

    public OtpSendingService getService() {
        return SpringContext.getApplicationContext(service);
    }

    public static OtpSendingChannel fromType(OtpSendingChannel type) {
        return fromType(type.name());
    }

    public static OtpSendingChannel fromType(String name) {
        return Arrays.stream(OtpSendingChannel.values())
                .filter(x -> x.name().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid OtpSendingType name: " + name));
    }
}
