package com.gucardev.wallet.domain.user.model.request;

import com.gucardev.wallet.domain.shared.model.request.BaseFilterRequest;
import com.gucardev.wallet.domain.auth.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterRequest extends BaseFilterRequest {

    @Schema(description = "Filter by name", example = "john")
    @Size(max = 255)
    private String name;

    @Schema(description = "Filter by surname", example = "doe")
    @Size(max = 255)
    private String surname;

    @Schema(description = "Filter by email", example = "johndoe@example.com")
    @Size(max = 255)
    private String email;

    @Schema(description = "Filter by role", example = "USER")
    @Pattern(regexp = "^(ADMIN|MODERATOR|MACHINE|USER)$", message = "{role.pattern.exception}")
    private String role;

    @Schema(description = "Filter by specific authority", example = "READ:USER")
    @Size(max = 255)
    private String authority;

    public Role getRole() {
        return role == null ? null : Role.valueOf(role);
    }
}
