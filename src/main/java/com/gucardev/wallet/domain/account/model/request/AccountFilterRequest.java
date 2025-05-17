package com.gucardev.wallet.domain.account.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gucardev.wallet.domain.account.enumeration.AccountType;
import com.gucardev.wallet.domain.shared.model.request.BaseFilterRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountFilterRequest extends BaseFilterRequest {

    @Schema(description = "Filter by account number", example = "82e923476b5449d5b385e7a6fdf5ce9e")
    @Size(max = 255)
    private String accountNumber;

    @Schema(description = "Filter by email", example = "john")
    @Size(max = 255)
    private String email;

    @Schema(description = "Filter by account type", example = "SAVINGS")
    @Pattern(regexp = "^(CHECKING|SAVINGS|CREDIT|INVESTMENT)$", message = "{role.pattern.exception}")
    private String accountType;

    @Schema(description = "Filter account created after this date", example = "2024-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "Filter account created before this date", example = "2025-12-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public AccountType getAccountType() {
        return accountType == null ? null : AccountType.valueOf(accountType);
    }
}
