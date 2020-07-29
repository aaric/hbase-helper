package com.incarcloud.helper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 数据删除功能配置
 *
 * @author Aaric, created on 2020-07-10T15:37.
 * @version 0.1.0-SNAPSHOT
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "helper.data-deletion")
public class DataDeletionConfig {

    /**
     * 数据表配置
     */
    private TableDelete table;

    /**
     * 迁移vin清单
     */
    private List<String> vinList;

    /**
     * 数据表配置类（正式库）
     */
    @Data
    public static class TableDelete {
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
