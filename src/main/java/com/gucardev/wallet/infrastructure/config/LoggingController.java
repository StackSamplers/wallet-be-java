package com.gucardev.wallet.infrastructure.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/logging")
public class LoggingController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LoggingController.class);
    private static final String PROJECT_ROOT_PACKAGE = determineProjectRootPackage();

    private static String determineProjectRootPackage() {
        String thisClassName = LoggingController.class.getName();
        String[] parts = thisClassName.split("\\.");
        if (parts.length >= 2) {
            return parts[0] + "." + parts[1] + "." + parts[2];
        }
        return "";
    }

    @GetMapping("/level/root/{level}")
    public ResponseEntity<Object> changeRootLogLevel(@PathVariable String level) {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        Level newLevel = Level.valueOf(level.toUpperCase());
        rootLogger.setLevel(newLevel);

        log.info("Root log level changed to: {}", level.toUpperCase());

        Map<String, String> response = new HashMap<>();
        response.put("logger", "ROOT");
        response.put("level", rootLogger.getLevel().toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/level/package")
    public ResponseEntity<Object> changePackageLogLevel(
            @RequestParam(required = false) String packageName,
            @RequestParam String level) {

        // Use project package as default if none provided
        String targetPackage = Optional.ofNullable(packageName)
                .filter(p -> !p.trim().isEmpty())
                .orElse(PROJECT_ROOT_PACKAGE);

        Logger logger = (Logger) LoggerFactory.getLogger(targetPackage);
        Level newLevel = Level.valueOf(level.toUpperCase());
        logger.setLevel(newLevel);

        log.info("Log level for package '{}' changed to: {}", targetPackage, level.toUpperCase());

        Map<String, String> response = new HashMap<>();
        response.put("logger", targetPackage);
        response.put("level", logger.getLevel().toString());
        response.put("isProjectDefault", String.valueOf(packageName == null || packageName.trim().isEmpty()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/levels")
    public ResponseEntity<Object> getLogLevels() {
        Map<String, String> levels = new HashMap<>();

        // Get root logger level
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        levels.put("ROOT", rootLogger.getLevel().toString());

        // Add project root package logger
        addLoggerIfExists(levels, PROJECT_ROOT_PACKAGE);

        // Add some common package loggers
        addLoggerIfExists(levels, "org.springframework");
        addLoggerIfExists(levels, "org.hibernate");
        addLoggerIfExists(levels, "org.apache.kafka");

        return ResponseEntity.ok(levels);
    }

    @GetMapping("/project-package")
    public ResponseEntity<Object> getProjectPackage() {
        Map<String, String> response = new HashMap<>();
        response.put("projectRootPackage", PROJECT_ROOT_PACKAGE);
        response.put("controllerPackage", LoggingController.class.getPackage().getName());
        return ResponseEntity.ok(response);
    }

    private void addLoggerIfExists(Map<String, String> levels, String loggerName) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        Level level = logger.getLevel();
        // Only add if the level is explicitly set (not inherited)
        if (level != null) {
            levels.put(loggerName, level.toString());
        } else {
            // Show effective level (inherited from parent)
            levels.put(loggerName, logger.getEffectiveLevel().toString() + " (inherited)");
        }
    }
}