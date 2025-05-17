package com.gucardev.wallet.domain.appconfig.enumeration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTypeTest {

    @ParameterizedTest
    @MethodSource("provideValidValues")
    void validateValue_ShouldReturnTrue_WhenValueIsValidForType(ConfigType type, String value) {
        assertTrue(type.validateValue(value));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValues")
    void validateValue_ShouldReturnFalse_WhenValueIsInvalidForType(ConfigType type, String value) {
        assertFalse(type.validateValue(value));
    }

    @Test
    void validateValue_ShouldReturnFalse_WhenValueIsNull() {
        for (ConfigType type : ConfigType.values()) {
            assertFalse(type.validateValue(null));
        }
    }

    private static Stream<Arguments> provideValidValues() {
        return Stream.of(
                // STRING type accepts any non-null string
                Arguments.of(ConfigType.STRING, ""),
                Arguments.of(ConfigType.STRING, "any string value"),
                Arguments.of(ConfigType.STRING, "123"),

                // INTEGER type
                Arguments.of(ConfigType.INTEGER, "0"),
                Arguments.of(ConfigType.INTEGER, "123"),
                Arguments.of(ConfigType.INTEGER, "-456"),
                Arguments.of(ConfigType.INTEGER, "  789  "), // Tests trim functionality

                // LONG type
                Arguments.of(ConfigType.LONG, "0"),
                Arguments.of(ConfigType.LONG, "9223372036854775807"), // Max long value
                Arguments.of(ConfigType.LONG, "-9223372036854775808"), // Min long value

                // DOUBLE type
                Arguments.of(ConfigType.DOUBLE, "0"),
                Arguments.of(ConfigType.DOUBLE, "0.0"),
                Arguments.of(ConfigType.DOUBLE, "123.45"),
                Arguments.of(ConfigType.DOUBLE, "-678.90"),
                Arguments.of(ConfigType.DOUBLE, "1E6"),

                // BOOLEAN type
                Arguments.of(ConfigType.BOOLEAN, "true"),
                Arguments.of(ConfigType.BOOLEAN, "false"),
                Arguments.of(ConfigType.BOOLEAN, "TRUE"),
                Arguments.of(ConfigType.BOOLEAN, "FALSE"),

                // BIG_DECIMAL type
                Arguments.of(ConfigType.BIG_DECIMAL, "0"),
                Arguments.of(ConfigType.BIG_DECIMAL, "123.456789"),
                Arguments.of(ConfigType.BIG_DECIMAL, "-987.654321"),

                // STRING_ARRAY type accepts any non-null string
                Arguments.of(ConfigType.STRING_ARRAY, ""),
                Arguments.of(ConfigType.STRING_ARRAY, "value1"),
                Arguments.of(ConfigType.STRING_ARRAY, "value1,value2,value3"),

                // INTEGER_ARRAY type
                Arguments.of(ConfigType.INTEGER_ARRAY, "1"),
                Arguments.of(ConfigType.INTEGER_ARRAY, "1,2,3,4,5"),
                Arguments.of(ConfigType.INTEGER_ARRAY, "-1,0,1")
        );
    }

    private static Stream<Arguments> provideInvalidValues() {
        return Stream.of(
                // INTEGER type
                Arguments.of(ConfigType.INTEGER, ""),
                Arguments.of(ConfigType.INTEGER, "not a number"),
                Arguments.of(ConfigType.INTEGER, "123.45"),
                Arguments.of(ConfigType.INTEGER, "123L"),
                Arguments.of(ConfigType.INTEGER, "2147483648"), // Exceeds Integer.MAX_VALUE

                // LONG type
                Arguments.of(ConfigType.LONG, ""),
                Arguments.of(ConfigType.LONG, "not a number"),
                Arguments.of(ConfigType.LONG, "123.45"),
                Arguments.of(ConfigType.LONG, "9223372036854775808"), // Exceeds Long.MAX_VALUE

                // DOUBLE type
                Arguments.of(ConfigType.DOUBLE, ""),
                Arguments.of(ConfigType.DOUBLE, "not a double"),
                Arguments.of(ConfigType.DOUBLE, "123.45.67"), // Invalid double format

                // BIG_DECIMAL type
                Arguments.of(ConfigType.BIG_DECIMAL, ""),
                Arguments.of(ConfigType.BIG_DECIMAL, "not a decimal"),
                Arguments.of(ConfigType.BIG_DECIMAL, "123.45.67"), // Invalid decimal format

                // INTEGER_ARRAY type
                Arguments.of(ConfigType.INTEGER_ARRAY, ""),
                Arguments.of(ConfigType.INTEGER_ARRAY, "1,two,3"),
                Arguments.of(ConfigType.INTEGER_ARRAY, "1,2.0,3")
        );
    }
}