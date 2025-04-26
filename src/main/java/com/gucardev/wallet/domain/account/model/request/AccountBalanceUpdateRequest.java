package com.gucardev.wallet.domain.account.model.request;

import com.gucardev.wallet.domain.transaction.enumeration.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceUpdateRequest {

    @NotNull
    private Long accountId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private TransactionType transactionType;
}
