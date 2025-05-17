package com.gucardev.wallet.domain.appconfig.service;

import com.gucardev.wallet.domain.appconfig.entity.AppConfig;
import com.gucardev.wallet.domain.appconfig.enumeration.ConfigType;
import com.gucardev.wallet.domain.appconfig.mapper.AppConfigMapper;
import com.gucardev.wallet.domain.appconfig.model.dto.AppConfigDto;
import com.gucardev.wallet.domain.appconfig.model.request.AppConfigRequest;
import com.gucardev.wallet.domain.appconfig.repository.AppConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AppConfigServiceTest {

    @Mock
    private AppConfigRepository configurationParameterRepository;

    @Mock
    private AppConfigMapper configurationParameterMapper;

    @InjectMocks
    private AppConfigService configurationParameterService;

    private static final Long ID = 1L;
    private static final String CONFIG_NAME = "testConfig";
    private static final String CONFIG_DESCRIPTION = "Test configuration";

    private AppConfig configEntity;
    private AppConfigDto configDto;
    private AppConfigRequest configRequest;

    @BeforeEach
    void setUp() {
        configEntity = new AppConfig();
        configEntity.setId(ID);
        configEntity.setKey(CONFIG_NAME);
        configEntity.setType(ConfigType.STRING);
        configEntity.setValue("test value");
        configEntity.setDescription(CONFIG_DESCRIPTION);
        configEntity.setDeleted(false);

        configDto = new AppConfigDto();
        configDto.setId(ID);
        configDto.setKey(CONFIG_NAME);
        configDto.setType(ConfigType.STRING);
        configDto.setValue("test value");
        configDto.setDescription(CONFIG_DESCRIPTION);

        configRequest = new AppConfigRequest();
        configRequest.setKey(CONFIG_NAME);
        configRequest.setType(ConfigType.STRING);
        configRequest.setValue("test value");
        configRequest.setDescription(CONFIG_DESCRIPTION);
    }

    @Test
    void findAll_ShouldReturnAllNonDeletedConfigurations() {
        // Arrange
        List<AppConfig> entities = Arrays.asList(configEntity);
        when(configurationParameterRepository.findByDeleted(false)).thenReturn(entities);
        when(configurationParameterMapper.toDto(configEntity)).thenReturn(configDto);

        // Act
        List<AppConfigDto> result = configurationParameterService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(ID, result.get(0).getId());
        verify(configurationParameterRepository).findByDeleted(false);
    }

    @Test
    void findByName_ShouldReturnConfigurationWhenExists() {
        // Arrange
        when(configurationParameterRepository.findByKeyAndDeletedFalse(CONFIG_NAME))
                .thenReturn(Optional.of(configEntity));
        when(configurationParameterMapper.toDto(configEntity)).thenReturn(configDto);

        // Act
        AppConfigDto result = configurationParameterService.findByName(CONFIG_NAME);

        // Assert
        assertEquals(ID, result.getId());
        assertEquals(CONFIG_NAME, result.getKey());
        verify(configurationParameterRepository).findByKeyAndDeletedFalse(CONFIG_NAME);
    }

    @Test
    void findByName_ShouldThrowExceptionWhenConfigurationDoesNotExist() {
        // Arrange
        when(configurationParameterRepository.findByKeyAndDeletedFalse(CONFIG_NAME))
                .thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                configurationParameterService.findByName(CONFIG_NAME)
        );
        // Don't check specific message content as it might be wrapped or transformed
        assertNotNull(exception.getMessage());
    }

    @Test
    void create_ShouldSaveConfigurationWhenValid() {
        // Arrange
        when(configurationParameterRepository.existsByKey(CONFIG_NAME)).thenReturn(false);
        when(configurationParameterMapper.toEntity(configRequest)).thenReturn(configEntity);

        // Act
        configurationParameterService.create(configRequest);

        // Assert
        verify(configurationParameterRepository).existsByKey(CONFIG_NAME);
        verify(configurationParameterMapper).toEntity(configRequest);
        verify(configurationParameterRepository).save(configEntity);
    }

    @Test
    void create_ShouldThrowExceptionWhenConfigurationNameExists() {
        // Arrange
        when(configurationParameterRepository.existsByKey(CONFIG_NAME)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                configurationParameterService.create(configRequest)
        );
        // Don't check specific message content
        assertNotNull(exception.getMessage());
        verify(configurationParameterRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("provideValidConfigTypeAndValueCombinations")
    void create_ShouldSaveConfiguration_WhenTypeAndValueAreValid(ConfigType type, String value) {
        // Arrange
        configRequest.setType(type);
        configRequest.setValue(value);

        when(configurationParameterRepository.existsByKey(CONFIG_NAME)).thenReturn(false);
        when(configurationParameterMapper.toEntity(configRequest)).thenReturn(configEntity);

        // Act
        configurationParameterService.create(configRequest);

        // Assert
        verify(configurationParameterRepository).save(any());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConfigTypeAndValueCombinations")
    void create_ShouldThrowException_WhenValueDoesNotMatchType(ConfigType type, String value) {
        // Arrange
        configRequest.setType(type);
        configRequest.setValue(value);

        when(configurationParameterRepository.existsByKey(CONFIG_NAME)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                configurationParameterService.create(configRequest)
        );
        // Don't check specific message content
        assertNotNull(exception.getMessage());
        verify(configurationParameterRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateConfigurationWhenValid() {
        // Arrange
        when(configurationParameterRepository.findById(ID)).thenReturn(Optional.of(configEntity));

        // Act
        configurationParameterService.update(ID, configRequest);

        // Assert
        verify(configurationParameterRepository).findById(ID);
        verify(configurationParameterRepository).save(configEntity);
        assertEquals(configRequest.getValue(), configEntity.getValue());
    }

    @Test
    void update_ShouldThrowExceptionWhenConfigurationDoesNotExist() {
        // Arrange
        when(configurationParameterRepository.findById(ID)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                configurationParameterService.update(ID, configRequest)
        );
        // Don't check specific message content
        assertNotNull(exception.getMessage());
        verify(configurationParameterRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConfigTypeAndValueCombinations")
    void update_ShouldThrowException_WhenValueDoesNotMatchType(ConfigType type, String value) {
        // Arrange
        configRequest.setType(type);
        configRequest.setValue(value);

        when(configurationParameterRepository.findById(ID)).thenReturn(Optional.of(configEntity));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                configurationParameterService.update(ID, configRequest)
        );
        // Don't check specific message content
        assertNotNull(exception.getMessage());
        verify(configurationParameterRepository, never()).save(any());
    }

    @Test
    void delete_ShouldMarkConfigurationAsDeletedWhenExists() {
        // Arrange
        when(configurationParameterRepository.findById(ID)).thenReturn(Optional.of(configEntity));

        // Act
        configurationParameterService.delete(ID);

        // Assert
        verify(configurationParameterRepository).findById(ID);
        verify(configurationParameterRepository).save(configEntity);
        assertTrue(configEntity.isDeleted());
    }

    @Test
    void delete_ShouldThrowExceptionWhenConfigurationDoesNotExist() {
        // Arrange
        when(configurationParameterRepository.findById(ID)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                configurationParameterService.delete(ID)
        );
        // Don't check specific message content
        assertNotNull(exception.getMessage());
        verify(configurationParameterRepository, never()).save(any());
    }

    private static Stream<Arguments> provideValidConfigTypeAndValueCombinations() {
        return Stream.of(
                Arguments.of(ConfigType.STRING, "any string value"),
                Arguments.of(ConfigType.INTEGER, "123"),
                Arguments.of(ConfigType.LONG, "123456789012"),
                Arguments.of(ConfigType.DOUBLE, "123.45"),
                Arguments.of(ConfigType.BOOLEAN, "true"),
                Arguments.of(ConfigType.BOOLEAN, "false"),
                Arguments.of(ConfigType.BIG_DECIMAL, "123456.789"),
                Arguments.of(ConfigType.STRING_ARRAY, "value1,value2,value3"),
                Arguments.of(ConfigType.INTEGER_ARRAY, "1,2,3,4,5")
        );
    }

    private static Stream<Arguments> provideInvalidConfigTypeAndValueCombinations() {
        return Stream.of(
                Arguments.of(ConfigType.INTEGER, "not a number"),
                Arguments.of(ConfigType.LONG, "123.45"),
                Arguments.of(ConfigType.DOUBLE, "not a double"),
                Arguments.of(ConfigType.BIG_DECIMAL, "not a decimal"),
                Arguments.of(ConfigType.INTEGER_ARRAY, "1,two,3")
        );
    }
}