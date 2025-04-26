package com.gucardev.wallet.domain.account.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceUpdateRequest {

    @NotNull
    private UUID accountId;
    @NotNull
    private BigDecimal amount;
//    @NotNull
//    private TransactionType transactionType;
}
