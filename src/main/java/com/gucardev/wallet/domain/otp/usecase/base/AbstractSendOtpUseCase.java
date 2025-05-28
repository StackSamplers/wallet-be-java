package com.gucardev.wallet.domain.otp.usecase.base;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.domain.otp.model.response.OtpResponse;
import com.gucardev.wallet.domain.otp.usecase.GenerateOtpUseCase;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSendOtpUseCase<T> implements UseCaseWithParamsAndReturn<T, OtpResponse> {

    protected final GenerateOtpUseCase generateOtpUseCase;

    @Override
    public OtpResponse execute(T params) {

        // Extract destination
        String destination = extractDestination(params);

        // Build OTP request
        OtpSendingRequest otpSendingRequest = buildOtpRequest(destination, params);

        // Generate OTP
        OtpResponse generatedOtp = generateOtpUseCase.execute(otpSendingRequest);

        // If already sent, return immediately
        if (generatedOtp.isSent()) {
            log.info("OTP already sent to destination: {}", destination);
            return generatedOtp;
        }

        // Set the generated OTP
        otpSendingRequest.setOtp(generatedOtp.getOtp());

        // Send OTP through appropriate channel
        sendOtp(otpSendingRequest);

        log.info("OTP sent successfully to destination: {} via {}",
                destination, getSendingChannel());

        return generatedOtp;
    }

    protected abstract String extractDestination(T params);
    protected abstract OtpType getOtpType();
    protected abstract OtpSendingChannel getSendingChannel();

    protected OtpSendingRequest buildOtpRequest(String destination, T params) {
        return OtpSendingRequest.builder()
                .type(getOtpType())
                .sendingChannel(getSendingChannel())
                .destination(destination)
                .build();
    }

    protected void sendOtp(OtpSendingRequest otpSendingRequest) {
        OtpSendingChannel.fromType(getSendingChannel())
                .getService()
                .sendNotification(otpSendingRequest);
    }
}