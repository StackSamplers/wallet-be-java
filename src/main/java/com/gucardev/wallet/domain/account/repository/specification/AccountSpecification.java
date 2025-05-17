package com.gucardev.wallet.domain.account.repository.specification;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.account.enumeration.AccountType;
import com.gucardev.wallet.domain.shared.repository.specification.BaseSpecification;
import com.gucardev.wallet.domain.transaction.entity.Transaction;
import com.gucardev.wallet.domain.user.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification extends BaseSpecification {

    public static Specification<Account> hasEmailLike(String email) {
        return (root, query, cb) -> {
            if (email == null) return null;
            Join<Account, User> userJoin = root.join("user", JoinType.INNER);
            return cb.like(cb.lower(userJoin.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<Account> hasAccountNumberLike(String accountNumber) {
        return BaseSpecification.like("accountNumber", accountNumber);
    }

    public static Specification<Account> hasAccountType(AccountType accountType) {
        return (root, query, cb) -> accountType == null ? null :
                cb.equal(root.get("accountType"), accountType);
    }

    public static Specification<Transaction> byAccountId(Long accountId) {
        return (root, query, cb) -> {
            if (accountId == null) return null;
            return cb.equal(root.get("account").get("id"), accountId);
        };
    }

}
