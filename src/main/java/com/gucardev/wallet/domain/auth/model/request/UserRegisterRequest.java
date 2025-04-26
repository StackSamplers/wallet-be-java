package com.gucardev.wallet.domain.auth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @Schema(
            description = "Password",
            example = "pass123",
            type = "string",
            minLength = 5,
            maxLength = 25
    )
    @Length(min = 5, max = 25)
    @NotNull
    private String password;

    @Schema(
            description = "Email Address",
            example = "johndoe@example.com",
            type = "string"
    )
    @Email
    @NotBlank
    private String email;

    @Schema(
            description = "First Name",
            example = "John",
            type = "string",
            minLength = 2,
            maxLength = 50
    )
    @Length(min = 2, max = 50)
    @NotNull
    private String name;

    @Schema(
            description = "Last Name",
            example = "John",
            type = "string",
            minLength = 2,
            maxLength = 50
    )
    @Length(min = 2, max = 50)
    @NotNull
    private String surname;

    @Schema(
            description = "Phone number (10 digits, no country code, no spaces, no leading 0)",
            example = "000000000",
            type = "string",
            minLength = 10,
            maxLength = 10
    )
    @Pattern(regexp = "^[1-9][0-9]{9}$", message = "Phone number must be exactly 10 digits, start with 1-9, and contain only numbers")
    @NotNull
    private String phoneNumber;

}
