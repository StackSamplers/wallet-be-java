package com.gucardev.wallet.domain.appconfig.service;

import com.gucardev.wallet.domain.appconfig.enumeration.ConfigType;
import com.gucardev.wallet.domain.appconfig.mapper.AppConfigMapper;
import com.gucardev.wallet.domain.appconfig.model.dto.AppConfigDto;
import com.gucardev.wallet.domain.appconfig.model.request.AppConfigRequest;
import com.gucardev.wallet.domain.appconfig.repository.AppConfigRepository;
import com.gucardev.wallet.infrastructure.config.cache.CacheNames;
import com.gucardev.wallet.infrastructure.config.cache.CaffeineCacheConfig;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppConfigService {

    private final AppConfigRepository appConfigRepository;
    private final AppConfigMapper appConfigMapper;


    @Cacheable(
            value = CacheNames.CACHE_CONFIGURATIONS,
            key = "#category",
            cacheManager = CaffeineCacheConfig.CACHE_MANAGER_MEDIUM_LIVED
    )
    public List<AppConfigDto> findAllByCategory(String category) {
        return appConfigRepository.findByDeletedAndCategory(false, category)
                .stream()
                .map(appConfigMapper::toDto)
                .toList();
    }

    @Cacheable(
            value = CacheNames.CACHE_CONFIGURATIONS,
            cacheManager = CaffeineCacheConfig.CACHE_MANAGER_MEDIUM_LIVED
    )
    public List<AppConfigDto> findAll() {
        return appConfigRepository.findByDeleted(false)
                .stream()
                .map(appConfigMapper::toDto)
                .toList();
    }

    @CacheEvict(
            value = CacheNames.CACHE_CONFIGURATIONS,
            allEntries = true,
            cacheManager = CaffeineCacheConfig.CACHE_MANAGER_MEDIUM_LIVED
    )
    public void create(AppConfigRequest request) {
        if (appConfigRepository.existsByKey(request.getKey())) {
            throw buildException(ExceptionMessage.ALREADY_EXISTS_EXCEPTION);
        }

        // Validate that the value matches the specified type
        if (configTypeAndValueDidNotMatch(request.getType(), request.getValue())) {
            throw buildException(ExceptionMessage.INVALID_VALUE_FOR_TYPE);
        }

        var entity = appConfigMapper.toEntity(request);
        entity.setDeleted(false);
        appConfigRepository.save(entity);
    }

    @CacheEvict(
            value = CacheNames.CACHE_CONFIGURATIONS,
            allEntries = true,
            cacheManager = CaffeineCacheConfig.CACHE_MANAGER_MEDIUM_LIVED
    )
    public void update(Long id, AppConfigRequest request) {
        var found = appConfigRepository.findById(id);
        if (found.isEmpty()) {
            throw buildException(ExceptionMessage.NOT_FOUND_EXCEPTION);
        }

        // Get the existing entity's config type if not provided in the request
        ConfigType configType = request.getType() != null
                ? request.getType()
                : found.get().getType();

        // Validate that the value matches the specified type
        if (configTypeAndValueDidNotMatch(configType, request.getValue())) {
            throw buildException(ExceptionMessage.INVALID_VALUE_FOR_TYPE);
        }

        found.get().setValue(request.getValue());
        if (request.getType() != null) {
            found.get().setType(request.getType());
        }
        appConfigRepository.save(found.get());
    }

    @CacheEvict(
            value = CacheNames.CACHE_CONFIGURATIONS,
            allEntries = true,
            cacheManager = CaffeineCacheConfig.CACHE_MANAGER_MEDIUM_LIVED
    )
    public void delete(Long id) {
        var found = appConfigRepository.findById(id);
        if (found.isEmpty()) {
            throw buildException(ExceptionMessage.NOT_FOUND_EXCEPTION);
        }
        found.get().setDeleted(true);
        appConfigRepository.save(found.get());
    }

    @Cacheable(
            value = CacheNames.CACHE_CONFIGURATIONS,
            key = "#configName",
            cacheManager = CaffeineCacheConfig.CACHE_MANAGER_MEDIUM_LIVED
    )
    public AppConfigDto findByName(String configName) {
        return appConfigRepository.findByKeyAndDeletedFalse(configName)
                .map(appConfigMapper::toDto)
                .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION));
    }

    private boolean configTypeAndValueDidNotMatch(ConfigType configType, String configValue) {
        if (configType == null) {
            return true;
        }
        return !configType.validateValue(configValue);
    }

    @CacheEvict(
            value = CacheNames.CACHE_CONFIGURATIONS,
            allEntries = true,
            cacheManager = CaffeineCacheConfig.CACHE_MANAGER_MEDIUM_LIVED
    )
    public void invalidateAllCache() {
        log.info("invalidateAllCache");
    }
}
