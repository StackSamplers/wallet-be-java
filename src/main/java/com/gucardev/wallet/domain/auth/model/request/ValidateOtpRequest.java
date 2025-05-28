package com.gucardev.wallet.domain.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateOtpRequest {

    @NotBlank(message = "destination is required")
    private String destination;

    @JsonIgnore
    private OtpType type;

    @JsonIgnore
    private OtpSendingChannel sendingChannel;

    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;
}
