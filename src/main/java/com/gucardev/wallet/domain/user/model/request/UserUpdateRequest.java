package com.gucardev.wallet.domain.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

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
            description = "Surname",
            example = "Smith",
            type = "string",
            minLength = 2,
            maxLength = 50
    )
    @Length(min = 2, max = 50)
    private String surname;

    @Schema(
            description = "phone number",
            example = "5431231234",
            type = "string",
            minLength = 3,
            maxLength = 15
    )
    @Length(min = 3, max = 15)
    @NotNull
    private String phoneNumber;
}
