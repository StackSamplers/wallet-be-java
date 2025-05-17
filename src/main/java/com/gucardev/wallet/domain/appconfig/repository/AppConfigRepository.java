package com.gucardev.wallet.domain.appconfig.repository;

import com.gucardev.wallet.domain.appconfig.entity.AppConfig;
import com.gucardev.wallet.domain.shared.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AppConfigRepository extends BaseJpaRepository<AppConfig, Long> {

    List<AppConfig> findByDeleted(boolean deleted);

    boolean existsByKey(String key);

    Optional<AppConfig> findByKeyAndDeletedFalse(String configName);

    List<AppConfig>  findByDeletedAndCategory(boolean deleted, String category);
}
