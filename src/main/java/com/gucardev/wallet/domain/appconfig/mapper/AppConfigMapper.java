package com.gucardev.wallet.domain.appconfig.mapper;


import com.gucardev.wallet.domain.appconfig.entity.AppConfig;
import com.gucardev.wallet.domain.appconfig.model.dto.AppConfigDto;
import com.gucardev.wallet.domain.appconfig.model.request.AppConfigRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppConfigMapper {

    AppConfigMapper INSTANCE = Mappers.getMapper(AppConfigMapper.class);

    AppConfig toEntity(AppConfigRequest request);

    AppConfigDto toDto(AppConfig entity);
}
