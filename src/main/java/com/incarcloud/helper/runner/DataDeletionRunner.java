package com.incarcloud.helper.runner;

import com.incarcloud.helper.config.DataDeletionConfig;
import com.incarcloud.helper.service.BigTableService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据删除功能初始化
 *
 * @author Aaric, created on 2020-07-10T15:54.
 * @version 0.1.0-SNAPSHOT
 */
@Log4j2
@Order(2)
@Component
public class DataDeletionRunner implements CommandLineRunner {

    /**
     * 数据删除功能配置
     */
    @Autowired
    private DataDeletionConfig dataDeletionConfig;

    /**
     * 大数据服务接口
     */
    @Autowired
    private BigTableService bigTableService;

    @Override
    public void run(String... args) throws Exception {
        // 记录日志
        log.info("Data deletion config, table={}, vinList={}",
                dataDeletionConfig.getTable(), dataDeletionConfig.getVinList());
    }
}
