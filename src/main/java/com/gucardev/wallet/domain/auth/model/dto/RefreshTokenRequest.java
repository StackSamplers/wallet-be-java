package com.gucardev.wallet.domain.auth.model.dto;

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
public class RefreshTokenRequest {

    @Schema(
            description = "refresh token",
            example = "f200da38-9f74-4867-a311-76e5542b4559",
            type = "string"
    )
    @NotBlank
    private String token;
}
