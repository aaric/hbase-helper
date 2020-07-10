package com.incarcloud.helper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 数据迁移功能配置
 *
 * @author Aaric, created on 2020-07-10T15:36.
 * @version 0.1.0-SNAPSHOT
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "helper.data-migration")
public class DataMigrationConfig {

    /**
     * 数据表配置
     */
    private TableConfig table;

    /**
     * 迁移vin清单
     */
    private List<String> vinList;

    /**
     * 数据表配置类
     */
    @Data
    public static class TableConfig {
        private String from;
        private String to;
    }
}
