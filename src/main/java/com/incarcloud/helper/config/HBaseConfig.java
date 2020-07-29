package com.incarcloud.helper.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * HBase配置
 *
 * @author Aaric, created on 2020-07-10T15:03.
 * @version 0.1.0-SNAPSHOT
 */
@Configuration
public class HBaseConfig {

    /**
     * 数据迁移功能配置
     */
    @Autowired
    private DataMigrationConfig dataMigrationConfig;

    /**
     * 数据删除功能配置
     */
    @Autowired
    private DataDeletionConfig dataDeletionConfig;

    /**
     * ZooKeeper服务端口
     */
    @Value("${incarcloud.hbase.zookeeper.property.clientPort}")
    private String zookeeperClientPort;

    /**
     * 初始化HBase连接对象（迁移From）
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Connection bigTableConnectionMigrationFrom() throws IOException {
        System.err.println("bigTableConnectionMigrationFrom -> " + dataMigrationConfig.getTableFrom().getZookeeper().getQuorum());

        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", dataMigrationConfig.getTableFrom().getZookeeper().getQuorum());
        configuration.set("hbase.zookeeper.property.clientPort", zookeeperClientPort);

        return ConnectionFactory.createConnection(configuration);
    }

    /**
     * 初始化HBase连接对象（迁移To）
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Connection bigTableConnectionMigrationTo() throws IOException {
        System.err.println("bigTableConnectionMigrationTo -> " + dataMigrationConfig.getTableTo().getZookeeper().getQuorum());

        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", dataMigrationConfig.getTableTo().getZookeeper().getQuorum());
        configuration.set("hbase.zookeeper.property.clientPort", zookeeperClientPort);

        return ConnectionFactory.createConnection(configuration);
    }

    /**
     * 初始化HBase连接对象（删除）
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Connection bigTableConnectionDeletion() throws IOException {
        System.err.println("bigTableConnectionDeletion -> " + dataDeletionConfig.getTable().getZookeeper().getQuorum());

        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", dataDeletionConfig.getTable().getZookeeper().getQuorum());
        configuration.set("hbase.zookeeper.property.clientPort", zookeeperClientPort);

        return ConnectionFactory.createConnection(configuration);
    }
}
