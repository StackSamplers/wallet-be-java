package com.gucardev.wallet.domain.otp.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterOtpRequest {
    private String destination;
}
