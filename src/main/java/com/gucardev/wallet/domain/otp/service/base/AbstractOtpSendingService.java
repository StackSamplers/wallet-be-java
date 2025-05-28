package com.gucardev.wallet.domain.otp.service.base;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOtpSendingService implements OtpSendingService {

    @Override
    public void sendNotification(OtpSendingRequest otpSendingRequest) {
        log.info("Sending OTP via {} to destination: {}",
                getSendingChannel(), otpSendingRequest.getDestination());

        validateRequest(otpSendingRequest);
        doSendNotification(otpSendingRequest);

        log.info("OTP sent successfully via {} to destination: {}",
                getSendingChannel(), otpSendingRequest.getDestination());
    }

    protected abstract OtpSendingChannel getSendingChannel();

    protected abstract void doSendNotification(OtpSendingRequest otpSendingRequest);

    protected void validateRequest(OtpSendingRequest request) {
        if (request == null || request.getDestination() == null || request.getOtp() == null) {
            throw new IllegalArgumentException("Invalid OTP sending request");
        }
    }
}