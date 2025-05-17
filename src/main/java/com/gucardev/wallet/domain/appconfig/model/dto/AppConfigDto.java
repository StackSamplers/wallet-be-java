package com.gucardev.wallet.domain.appconfig.model.dto;

import com.gucardev.wallet.domain.appconfig.enumeration.ConfigType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppConfigDto {

    private Long id;
    private String category;
    private String key;
    private String value;
    private String description;
    private ConfigType type;

    public boolean isActive() {
        return asBoolean();
    }

    public boolean isActive(boolean defaultValue) {
        return asBoolean(defaultValue);
    }

    public Integer asInteger(Integer defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Integer asInteger() {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Config value is null or empty");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Config value cannot be converted to Integer: " + value, e);
        }
    }

    public Long asLong(Long defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Long asLong() {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Config value is null or empty");
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Config value cannot be converted to Long: " + value, e);
        }
    }

    public Double asDouble(Double defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Double asDouble() {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Config value is null or empty");
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Config value cannot be converted to Double: " + value, e);
        }
    }

    public BigDecimal asBigDecimal(BigDecimal defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public BigDecimal asBigDecimal() {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Config value is null or empty");
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Config value cannot be converted to BigDecimal: " + value, e);
        }
    }

    public Boolean asBoolean(Boolean defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    public Boolean asBoolean() {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(value.trim());
    }

    public String[] asStringArray(String delimiter, String[] defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        // Split using regex pattern - need to escape special regex characters
        // For common delimiters like |, ., +, etc.
        return value.split(Pattern.quote(delimiter));
    }

    public String[] asStringArray(String[] defaultValue) {
        return asStringArray(",", defaultValue);
    }

    public String[] asStringArray() {
        if (value == null || value.isEmpty()) {
            return new String[0];
        }
        return value.split(",");
    }

    public Integer[] asIntegerArray(String delimiter, Integer[] defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            String[] parts = value.split(delimiter);
            return Arrays.stream(parts)
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Integer[] asIntegerArray(Integer[] defaultValue) {
        return asIntegerArray(",", defaultValue);
    }

    public Integer[] asIntegerArray() {
        if (value == null || value.isEmpty()) {
            return new Integer[0];
        }
        try {
            String[] parts = value.split(",");
            return Arrays.stream(parts)
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Config value cannot be converted to Integer array: " + value, e);
        }
    }
}
