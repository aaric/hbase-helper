package com.incarcloud.helper.runner;

import com.incarcloud.helper.config.DataMigrationConfig;
import com.incarcloud.helper.service.BigTableService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据迁移功能初始化
 *
 * @author Aaric, created on 2020-07-10T15:47.
 * @version 0.1.0-SNAPSHOT
 */
@Log4j2
@Order(1)
@Component
public class DataMigrationRunner implements CommandLineRunner {

    /**
     * 数据迁移功能配置
     */
    @Autowired
    private DataMigrationConfig dataMigrationConfig;

    /**
     * 大数据服务接口
     */
    @Autowired
    private BigTableService bigTableService;

    @Override
    public void run(String... args) throws Exception {
        // 记录日志
        log.info("Data migration config, table=(from={}, to={}), vinList={}",
                dataMigrationConfig.getTable().getFrom(), dataMigrationConfig.getTable().getTo(), dataMigrationConfig.getVinList());

        // **开始作业**
        log.info("Data migration starting...");

        // **作业内容**

        // **完成作业**
        log.info("Data migration finished.");
    }
}
