package com.gucardev.wallet.domain.transaction.model.dto;

import com.gucardev.wallet.domain.transaction.enumeration.TransactionType;
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
public class TransactionDto {

    private Long id;

    private BigDecimal amount;

    private String description;

    private LocalDateTime transactionDate;

    private TransactionType type;

    private Long accountId;

//    private Set<Category> categories = new HashSet<>();

}
