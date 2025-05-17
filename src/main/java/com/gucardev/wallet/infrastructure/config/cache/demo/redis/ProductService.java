package com.gucardev.wallet.infrastructure.config.cache.demo.redis;

import com.gucardev.wallet.infrastructure.config.cache.CacheNames;
import com.gucardev.wallet.infrastructure.config.cache.redis.RedisCacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductService {

    // Simulating a database with a HashMap
    private final Map<Long, Product> productDB = new HashMap<>();

    public ProductService() {
        // Adding some initial data
        productDB.put(1L, new Product(1L, "Laptop", 1299.99));
        productDB.put(2L, new Product(2L, "Phone", 799.99));
        productDB.put(3L, new Product(3L, "Tablet", 499.99));
    }

    @Cacheable(value = CacheNames.CACHE_PRODUCTS, key = "#id", cacheManager = RedisCacheConfig.REDIS_CACHE_MANAGER_MEDIUM_LIVED)
    public Product getProduct(Long id) {
        log.info("Fetching product from DB for id: {}", id);
        // Simulate slow database access
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return productDB.get(id);
    }

    @CachePut(value = CacheNames.CACHE_PRODUCTS, key = "#product.id", cacheManager = RedisCacheConfig.REDIS_CACHE_MANAGER_MEDIUM_LIVED)
    public Product updateProduct(Product product) {
        log.info("Updating product in DB: {}", product.getId());
        productDB.put(product.getId(), product);
        return product;
    }

    @CacheEvict(value = CacheNames.CACHE_PRODUCTS, key = "#id", cacheManager = RedisCacheConfig.REDIS_CACHE_MANAGER_MEDIUM_LIVED)
    public void deleteProduct(Long id) {
        log.info("Deleting product from DB: {}", id);
        productDB.remove(id);
    }

    @CacheEvict(value = CacheNames.CACHE_PRODUCTS, allEntries = true, cacheManager = RedisCacheConfig.REDIS_CACHE_MANAGER_MEDIUM_LIVED)
    public void clearCache() {
        log.info("Clearing all products from cache");
        // This method will clear all entries from the "products" cache
    }

    @Cacheable(value = CacheNames.CACHE_PRODUCTS, cacheManager = RedisCacheConfig.REDIS_CACHE_MANAGER_MEDIUM_LIVED)
    public List<Product> getAllProducts() {
        return new ArrayList<>(productDB.values());
    }
}