package com.gucardev.wallet.domain.transaction.repository;

import com.gucardev.wallet.domain.shared.repository.BaseJpaRepository;
import com.gucardev.wallet.domain.transaction.entity.Transaction;
import org.springframework.stereotype.Repository;



@Repository
public interface TransactionRepository extends BaseJpaRepository<Transaction, Long> {

}
