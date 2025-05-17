package com.gucardev.wallet.domain.appconfig.entity;

import com.gucardev.wallet.domain.appconfig.enumeration.ConfigType;
import com.gucardev.wallet.domain.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "app_configuration")
@Entity
public class AppConfig extends BaseEntity {

    private String category;

    @Column(nullable = false, unique = true, name = "config_key")
    private String key;
    @Column(nullable = false, length = 1000, name = "config_value")
    private String value;
    private String description;

    @Enumerated(EnumType.STRING)
    private ConfigType type;
}