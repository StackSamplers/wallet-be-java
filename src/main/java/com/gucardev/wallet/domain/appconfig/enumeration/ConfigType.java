package com.gucardev.wallet.domain.appconfig.enumeration;

import java.math.BigDecimal;

public enum ConfigType {
    STRING,
    INTEGER,
    LONG,
    DOUBLE,
    BOOLEAN,
    BIG_DECIMAL,
    STRING_ARRAY,
    INTEGER_ARRAY;

    public boolean validateValue(String value) {
        if (value == null) {
            return false;
        }

        try {
            switch (this) {
                case STRING, STRING_ARRAY:
                    return true;
                case INTEGER:
                    Integer.parseInt(value.trim());
                    return true;
                case LONG:
                    Long.parseLong(value.trim());
                    return true;
                case DOUBLE:
                    Double.parseDouble(value.trim());
                    return true;
                case BOOLEAN:
                    return value.trim().equalsIgnoreCase("true")
                            || value.trim().equalsIgnoreCase("false");
                case BIG_DECIMAL:
                    new BigDecimal(value.trim());
                    return true;
                case INTEGER_ARRAY:
                    String[] parts = value.split(",");
                    for (String part : parts) {
                        Integer.parseInt(part.trim());
                    }
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}