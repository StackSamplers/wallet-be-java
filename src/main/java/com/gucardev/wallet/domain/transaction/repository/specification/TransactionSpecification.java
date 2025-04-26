package com.gucardev.wallet.domain.transaction.repository.specification;

import com.gucardev.wallet.domain.shared.repository.specification.BaseSpecification;
import com.gucardev.wallet.domain.transaction.entity.Transaction;
import com.gucardev.wallet.domain.transaction.enumeration.TransactionType;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification extends BaseSpecification {

    public static Specification<Transaction> hasTransactionType(TransactionType transactionType) {
        return (root, query, cb) -> transactionType == null ? null :
                cb.equal(root.get("type"), transactionType);
    }

}
