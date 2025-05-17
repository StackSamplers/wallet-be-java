package com.gucardev.wallet.domain.appconfig.controller;

import com.gucardev.wallet.domain.appconfig.model.dto.AppConfigDto;
import com.gucardev.wallet.domain.appconfig.model.request.AppConfigRequest;
import com.gucardev.wallet.domain.appconfig.service.AppConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app-config")
@RequiredArgsConstructor
public class AppConfigControllerV1 {

    private final AppConfigService service;

    @GetMapping("/all")
    public ResponseEntity<List<AppConfigDto>> getAllConfigurationParameters() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/all/by-category/{categoryName}")
    public ResponseEntity<List<AppConfigDto>> getAllConfigurationParameters(@PathVariable String categoryName) {
        return ResponseEntity.ok(service.findAllByCategory(categoryName));
    }

    @PostMapping
    public ResponseEntity<AppConfigDto> create(@RequestBody AppConfigRequest request) {
        service.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppConfigDto> create(@RequestBody AppConfigRequest request, @PathVariable Long id) {
        service.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppConfigDto> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/invalidate-all-cache")
    public ResponseEntity<AppConfigDto> invalidateAllCache() {
        service.invalidateAllCache();
        return ResponseEntity.ok().build();
    }

}
