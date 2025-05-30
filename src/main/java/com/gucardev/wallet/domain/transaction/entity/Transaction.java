package com.gucardev.wallet.domain.transaction.entity;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.shared.entity.BaseEntity;
import com.gucardev.wallet.domain.transaction.enumeration.TransactionSource;
import com.gucardev.wallet.domain.transaction.enumeration.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Helper method to set the account relationship
    public void setAccount(Account account) {
        this.account = account;

        // Add this transaction to the account's transactions collection if not null
        if (account != null) {
            account.addTransaction(this);
        }
    }

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "transaction_categories",
//            joinColumns = @JoinColumn(name = "transaction_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id")
//    )
//    private Set<Category> categories = new HashSet<>();
}