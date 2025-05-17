package com.gucardev.wallet.infrastructure.config.cache.redis.persist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/uuid-cache")
@RequiredArgsConstructor
public class UuidCacheController {

    private final UuidCacheService uuidCacheService;

    @PostMapping("/{uuid}")
    public ResponseEntity<String> storeUuid(
            @PathVariable String uuid,
            @RequestParam(defaultValue = "300") long ttlSeconds) {

        boolean success = uuidCacheService.storeUuid(uuid, ttlSeconds);
        if (success) {
            return ResponseEntity.ok("UUID stored with TTL: " + ttlSeconds + " seconds");
        } else {
            return ResponseEntity.internalServerError().body("Failed to store UUID");
        }
    }

    @GetMapping("/{uuid}/exists")
    public ResponseEntity<Boolean> checkUuidExists(@PathVariable String uuid) {
        boolean exists = uuidCacheService.existsUuid(uuid);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteUuid(@PathVariable String uuid) {
        boolean deleted = uuidCacheService.deleteUuid(uuid);
        if (deleted) {
            return ResponseEntity.ok("UUID deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Set<String>> getAllUuids() {
        Set<String> uuids = uuidCacheService.getAllUuids();
        return ResponseEntity.ok(uuids);
    }

    @GetMapping("/{uuid}/ttl")
    public ResponseEntity<Long> getUuidTTL(@PathVariable String uuid) {
        long ttl = uuidCacheService.getUuidTimeToLive(uuid);
        if (ttl >= -1) {  // -1 means no expiry, -2 means key doesn't exist
            return ResponseEntity.ok(ttl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{uuid}/ttl/{seconds}")
    public ResponseEntity<String> updateUuidTTL(
            @PathVariable String uuid,
            @PathVariable long seconds) {

        boolean updated = uuidCacheService.updateUuidTTL(uuid, seconds);
        if (updated) {
            return ResponseEntity.ok("TTL updated to " + seconds + " seconds");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}