package com.gucardev.wallet.domain.user.repository;

import com.gucardev.wallet.domain.shared.repository.BaseJpaRepository;
import com.gucardev.wallet.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseJpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
