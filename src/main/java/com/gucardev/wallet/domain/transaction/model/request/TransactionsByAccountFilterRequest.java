package com.gucardev.wallet.domain.transaction.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gucardev.wallet.domain.shared.model.request.BaseFilterRequest;
import com.gucardev.wallet.domain.transaction.enumeration.TransactionType;
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
public class TransactionsByAccountFilterRequest extends BaseFilterRequest {

    @Schema(description = "Filter by account id", example = "123")
    private Long accountId;

    @Schema(description = "Filter by account type", example = "DEPOSIT")
    @Pattern(regexp = "^(DEPOSIT|WITHDRAWAL|TRANSFER|PAYMENT)$", message = "{role.pattern.exception}")
    private String transactionType;

    @Schema(description = "Filter transactions created after this date", example = "2024-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "Filter transactions created before this date", example = "2025-12-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public TransactionType getTransactionType() {
        return transactionType == null ? null : TransactionType.valueOf(transactionType);
    }

}
