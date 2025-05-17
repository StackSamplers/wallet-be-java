package com.gucardev.wallet.domain.appconfig.model.request;

import com.gucardev.wallet.domain.appconfig.enumeration.ConfigType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class AppConfigRequest {

    private String category = "default";
    private String key;
    private String value;
    private String description;
    private ConfigType type;
}