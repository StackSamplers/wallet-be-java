package com.gucardev.wallet.domain.account.entity;

import com.gucardev.wallet.domain.account.enumeration.AccountType;
import com.gucardev.wallet.domain.shared.entity.BaseEntity;
import com.gucardev.wallet.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    public void addUser(User user) {
        this.user = user;
    }

//    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Transaction> transactions = new ArrayList<>();
}
