package com.gucardev.wallet.domain.user.repository;

import com.gucardev.wallet.domain.shared.repository.BaseJpaRepository;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.enumeration.Role;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseJpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRolesIn(Collection<Role> roles);

    boolean existsByEmail(String email);
}
