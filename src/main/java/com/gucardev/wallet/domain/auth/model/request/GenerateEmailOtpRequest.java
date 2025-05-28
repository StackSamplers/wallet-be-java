package com.gucardev.wallet.domain.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateEmailOtpRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @JsonIgnore
    private OtpType type;
}


