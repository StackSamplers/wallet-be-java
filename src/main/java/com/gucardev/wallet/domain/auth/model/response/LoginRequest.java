package com.gucardev.wallet.domain.auth.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Schema(
            description = "email",
            example = "johndoe@example.com",
            type = "string"
    )
    @NotBlank
    private String email;
    @NotBlank
    @Schema(
            description = "password",
            example = "pass123",
            type = "string"
    )
    private String password;
}
