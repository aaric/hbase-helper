package com.incarcloud.helper.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
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
     * ZooKeeper主机地址
     */
    @Value("${incarcloud.hbase.zookeeper.quorum}")
    private String zookeeperQuorum;

    /**
     * ZooKeeper服务端口
     */
    @Value("${incarcloud.hbase.zookeeper.property.clientPort}")
    private String zookeeperClientPort;

    /**
     * 初始化HBase操作对象
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Connection bigTableConnection() throws IOException {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", zookeeperQuorum);
        configuration.set("hbase.zookeeper.property.clientPort", zookeeperClientPort);

        return ConnectionFactory.createConnection(configuration);
    }
}
