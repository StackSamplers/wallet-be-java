package com.gucardev.wallet.domain.user.entity;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.shared.entity.BaseEntity;
import com.gucardev.wallet.domain.user.enumeration.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private Boolean activated;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    public Boolean getActivated() {
        return activated != null && activated;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Account account;

    // Helper method to set the account relationship
    public void addAccount(Account account) {
        // Remove old bidirectional reference
        if (this.account != null) {
            this.account.setUser(null);
        }

        // Set new account
        this.account = account;

        // Set the bidirectional reference
        if (account != null) {
            account.addUser(this);
        }
    }

    // Helper method to check if user has an account
    public boolean hasAccount() {
        return this.account != null;
    }

}