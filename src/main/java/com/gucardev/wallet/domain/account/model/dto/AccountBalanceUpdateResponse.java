package com.gucardev.wallet.domain.account.model.dto;

import com.gucardev.wallet.domain.transaction.enumeration.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceUpdateResponse {

    private Long id;
    private BigDecimal previousBalance;
    private BigDecimal newBalance;
    private BigDecimal changeAmount;
    private TransactionType transactionType;
    private LocalDateTime updatedAt;

}
