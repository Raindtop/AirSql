package org.airsql.dataSource.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigType {
    MASTER("master", 1),
    SLAVE("slave", 2);

    private String name;
    private Integer value;

    public ConfigType valueToEnum(Object value){
        if (value instanceof String || value instanceof Integer){
            for (ConfigType configType: ConfigType.values()){
                if (value instanceof String && value.equals(configType.name)){
                    return configType;
                }
                if (value instanceof Integer && ((Integer) value).equals(configType.value)){
                    return configType;
                }
            }
        }
        return null;
    }
}
