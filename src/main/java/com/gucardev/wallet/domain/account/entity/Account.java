package com.gucardev.wallet.domain.account.entity;

import com.gucardev.wallet.domain.account.enumeration.AccountType;
import com.gucardev.wallet.domain.shared.entity.BaseEntity;
import com.gucardev.wallet.domain.transaction.entity.Transaction;
import com.gucardev.wallet.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity {

    // version for optimistic locking
    @Version
    private Long version;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);

        // Set the bidirectional reference if it's not already set
        if (transaction.getAccount() != this) {
            transaction.setAccount(this);
        }
    }

    // Helper method to remove a transaction
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);

        // Remove the bidirectional reference
        transaction.setAccount(null);
    }

//    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Transaction> transactions = new ArrayList<>();
}
