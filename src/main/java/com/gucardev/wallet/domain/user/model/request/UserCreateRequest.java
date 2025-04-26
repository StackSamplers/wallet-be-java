package com.gucardev.wallet.domain.user.model.request;

import com.gucardev.wallet.domain.auth.model.request.UserRegisterRequest;
import com.gucardev.wallet.domain.user.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

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
            description = "Surname",
            example = "Doe",
            type = "string",
            minLength = 2,
            maxLength = 50
    )
    @Length(min = 2, max = 50)
    private String surname;

    @Schema(
            description = "phone number",
            example = "05431231234",
            type = "string",
            minLength = 3,
            maxLength = 15
    )
    @Length(min = 3, max = 15)
    @NotNull
    private String phoneNumber;

    private Set<Role> roles = new HashSet<>();


    public UserCreateRequest(@Valid UserRegisterRequest userRegisterRequest) {
        this.name = userRegisterRequest.getName();
        this.surname = userRegisterRequest.getSurname();
        this.email = userRegisterRequest.getEmail();
        this.password = userRegisterRequest.getPassword();
        this.phoneNumber = userRegisterRequest.getPhoneNumber();
        this.roles = Set.of(Role.USER);
    }

}
