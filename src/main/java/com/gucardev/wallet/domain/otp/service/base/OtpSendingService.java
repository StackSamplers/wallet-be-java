package com.gucardev.wallet.domain.otp.service.base;


import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;

public interface OtpSendingService {

    void sendNotification(OtpSendingRequest otpSendingRequest);

}
