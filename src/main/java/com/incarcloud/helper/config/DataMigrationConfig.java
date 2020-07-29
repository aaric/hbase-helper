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
     * 压测库配置
     */
    private TableFrom tableFrom;

    /**
     * 正式库配置
     */
    private TableFrom tableTo;

    /**
     * 迁移vin清单
     */
    private List<String> vinList;

    /**
     * 数据表配置类（正式库）
     */
    @Data
    public static class TableFrom {
        private String name;
        private ZookeeperQuorum zookeeper;
    }

    /**
     * 数据表配置类（正式库）
     */
    @Data
    public static class TableTo {
        private String name;
        private ZookeeperQuorum zookeeper;
    }

    /**
     * Zookeeper配置
     */
    @Data
    public static class ZookeeperQuorum {
        private String quorum;
    }
}
