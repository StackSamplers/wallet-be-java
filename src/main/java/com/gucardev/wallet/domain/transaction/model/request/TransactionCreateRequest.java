package com.gucardev.wallet.domain.transaction.model.request;

import com.gucardev.wallet.domain.transaction.enumeration.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateRequest {

    @Schema(description = "Account ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull
    private Long accountId;

    @Schema(description = "Transaction amount", example = "100.50", type = "number")
    @NotNull
    @Positive
    @Min(1)
    private BigDecimal amount;

    @Schema(description = "Transaction description", example = "Monthly grocery shopping")
    private String description;

    @Schema(description = "Transaction date", example = "2023-04-15T14:30:00")
    @NotNull
    private LocalDateTime transactionDate;

    @Schema(
            description = "Transaction type",
            example = "DEPOSIT",
            type = "string",
            allowableValues = {"DEPOSIT", "WITHDRAWAL", "PAYMENT", "TRANSFER"}
    )
    @Pattern(regexp = "^(DEPOSIT|WITHDRAWAL|PAYMENT|TRANSFER)$", message = "{transactionType.pattern.exception}")
    @NotNull
    private String type;

    public TransactionType getType() {
        return type == null ? null : TransactionType.valueOf(type);
    }

    //    private Set<Long> categoryIds;
}