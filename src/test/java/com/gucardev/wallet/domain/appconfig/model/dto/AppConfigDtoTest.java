package com.gucardev.wallet.domain.appconfig.model.dto;

import com.gucardev.wallet.domain.appconfig.enumeration.ConfigType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


import java.math.BigDecimal;

class AppConfigDtoTest {

    private AppConfigDto dto;
    private static final Long ID = 1L;
    private static final String CONFIG_NAME = "testConfig";

    @BeforeEach
    void setUp() {
        dto = new AppConfigDto();
        dto.setId(ID);
        dto.setKey(CONFIG_NAME);
        dto.setDescription("Test description");
    }

    @Test
    void asInteger_ShouldReturnInteger_WhenConfigValueIsValidInteger() {
        // Arrange
        dto.setValue("123");
        dto.setType(ConfigType.INTEGER);

        // Act
        Integer result = dto.asInteger();

        // Assert
        assertEquals(123, result);
    }

    @Test
    void asInteger_ShouldReturnDefaultValue_WhenConfigValueIsEmpty() {
        // Arrange
        dto.setValue("");
        dto.setType(ConfigType.INTEGER);

        // Act
        Integer result = dto.asInteger(42);

        // Assert
        assertEquals(42, result);
    }

    @Test
    void asInteger_ShouldThrowException_WhenConfigValueIsInvalidInteger() {
        // Arrange
        dto.setValue("not an integer");
        dto.setType(ConfigType.INTEGER);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> dto.asInteger());
        assertTrue(exception.getMessage().contains("cannot be converted to Integer"));
    }

    @Test
    void asLong_ShouldReturnLong_WhenConfigValueIsValidLong() {
        // Arrange
        dto.setValue("123456789012");
        dto.setType(ConfigType.LONG);

        // Act
        Long result = dto.asLong();

        // Assert
        assertEquals(123456789012L, result);
    }

    @Test
    void asDouble_ShouldReturnDouble_WhenConfigValueIsValidDouble() {
        // Arrange
        dto.setValue("123.45");
        dto.setType(ConfigType.DOUBLE);

        // Act
        Double result = dto.asDouble();

        // Assert
        assertEquals(123.45, result);
    }

    @Test
    void asBigDecimal_ShouldReturnBigDecimal_WhenConfigValueIsValidBigDecimal() {
        // Arrange
        dto.setValue("123.456789");
        dto.setType(ConfigType.BIG_DECIMAL);

        // Act
        BigDecimal result = dto.asBigDecimal();

        // Assert
        assertEquals(new BigDecimal("123.456789"), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "TRUE", "True"})
    void asBoolean_ShouldReturnTrue_WhenConfigValueIsVariationOfTrue(String boolValue) {
        // Arrange
        dto.setValue(boolValue);
        dto.setType(ConfigType.BOOLEAN);

        // Act
        Boolean result = dto.asBoolean();

        // Assert
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "FALSE", "False", "anything else"})
    void asBoolean_ShouldReturnFalse_WhenConfigValueIsNotTrue(String boolValue) {
        // Arrange
        dto.setValue(boolValue);
        dto.setType(ConfigType.BOOLEAN);

        // Act
        Boolean result = dto.asBoolean();

        // Assert
        assertFalse(result);
    }

    @Test
    void asStringArray_ShouldReturnStringArray_WhenConfigValueContainsCommas() {
        // Arrange
        dto.setValue("value1,value2,value3");
        dto.setType(ConfigType.STRING_ARRAY);

        // Act
        String[] result = dto.asStringArray();

        // Assert
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }

    @Test
    void asStringArray_WithCustomDelimiter_ShouldReturnStringArray() {
        // Arrange
        dto.setValue("value1|value2|value3");
        dto.setType(ConfigType.STRING_ARRAY);

        // Act
        String[] result = dto.asStringArray("|", new String[0]);

        // Assert
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }


    @Test
    void asIntegerArray_ShouldReturnIntegerArray_WhenConfigValueContainsValidIntegers() {
        // Arrange
        dto.setValue("1,2,3,4,5");
        dto.setType(ConfigType.INTEGER_ARRAY);

        // Act
        Integer[] result = dto.asIntegerArray();

        // Assert
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    void asIntegerArray_ShouldThrowException_WhenConfigValueContainsInvalidIntegers() {
        // Arrange
        dto.setValue("1,two,3");
        dto.setType(ConfigType.INTEGER_ARRAY);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> dto.asIntegerArray());
        assertTrue(exception.getMessage().contains("cannot be converted to Integer array"));
    }

    @Test
    void isActive_ShouldReturnTrue_WhenConfigValueIsTrue() {
        // Arrange
        dto.setValue("true");
        dto.setType(ConfigType.BOOLEAN);

        // Act & Assert
        assertTrue(dto.isActive());
    }

    @Test
    void isActive_ShouldReturnFalse_WhenConfigValueIsFalse() {
        // Arrange
        dto.setValue("false");
        dto.setType(ConfigType.BOOLEAN);

        // Act & Assert
        assertFalse(dto.isActive());
    }

    @Test
    void isActive_WithDefaultValue_ShouldReturnDefaultValue_WhenConfigValueIsEmpty() {
        // Arrange
        dto.setValue("");
        dto.setType(ConfigType.BOOLEAN);

        // Act & Assert
        assertTrue(dto.isActive(true));
        assertFalse(dto.isActive(false));
    }
}