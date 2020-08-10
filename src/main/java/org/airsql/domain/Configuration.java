package org.airsql.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 连接属性
 * </p>
 *
 * @author raindrop
 * @since 2020/8/4
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    /**
     * 配置ID(唯一）
     */
    private String configId;

    private String url;

    private String user;

    private String password;

    private String driverClass;
}
