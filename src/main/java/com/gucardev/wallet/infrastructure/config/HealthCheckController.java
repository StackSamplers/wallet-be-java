package com.gucardev.wallet.infrastructure.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.gucardev.wallet.infrastructure.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class HealthCheckController {

    @GetMapping({"/", "/hello"})
    public ResponseEntity<?> helloWorld() {
        log.info("Hello World");
        return SuccessResponse.builder().body("Hello World!").build();
    }

    @GetMapping("/time")
    public ResponseEntity<Map<String, Object>> getTimezone() {
        Map<String, String> response = new HashMap<>();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        response.put("timezone", zoneId.getId());
        response.put("offset", zoneId.getRules().getOffset(now.toInstant()).getId());
        response.put("currentTime", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return SuccessResponse.builder().body(response).build();
    }

    @GetMapping("/log-level")
    public ResponseEntity<Object> getLogLevel() {
        return SuccessResponse.builder().body(getLogLevelValue()).build();
    }

    @GetMapping("/log-level/{level}")
    public ResponseEntity<Object> getLogLevel(@PathVariable String level) {
        Logger rootLogger = getRootLogger();
        Level newLevel = Level.valueOf(level.toUpperCase());
        rootLogger.setLevel(newLevel);
        log.info("Log level changed to: {}", level.toUpperCase());
        return SuccessResponse.builder().body(getLogLevelValue()).build();
    }

    private static Map<String, String> getLogLevelValue() {
        return Map.of("log-level", getRootLogger().getLevel().toString());
    }

    private static Logger getRootLogger() {
        return (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

}
