package com.gucardev.wallet.domain.account.model.request;

import com.gucardev.wallet.domain.account.enumeration.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequest {

    @Schema(description = "Initial balance amount", example = "10.20", type = "number")
    @NotNull(message = "Initial balance is required")
    @Positive(message = "Initial balance must be positive")
    private BigDecimal initialBalance;

    @Schema(
            description = "account type",
            example = "SAVINGS",
            type = "string",
            allowableValues = {"CHECKING", "SAVINGS", "CREDIT", "INVESTMENT"}
    )
    @Pattern(regexp = "^(CHECKING|SAVINGS|CREDIT|INVESTMENT)$", message = "{accountType.pattern.exception}")
    @NotNull(message = "Account type is required")
    private String accountType;

    @NotNull(message = "User ID is required")
    private Long userId;

    public AccountType getAccountType() {
        return accountType == null ? null : AccountType.valueOf(accountType);
    }
}
