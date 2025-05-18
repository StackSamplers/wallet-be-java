package com.gucardev.wallet.domain.auth.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Data
@NoArgsConstructor
public class OtpResponse {
    private String otp;
    private LocalDateTime expiryTime;
    private OtpType type;
    @JsonIgnore
    private String phoneNumber;
    @JsonIgnore
    private boolean sent = true;

    public OtpResponse(String otp, LocalDateTime expiryTime, OtpType type, String phoneNumber, boolean sent) {
        this.otp = otp;
        this.expiryTime = expiryTime;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.sent = sent;
    }
}