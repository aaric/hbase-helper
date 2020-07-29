package com.incarcloud.helper.runner;

import com.incarcloud.boar.bigtable.IBigTable;
import com.incarcloud.helper.config.DataMigrationConfig;
import com.incarcloud.helper.service.BigTableService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * HBase连接对象（迁移From）
     */
    @Autowired
    @Qualifier("bigTableConnectionMigrationFrom")
    private Connection bigTableConnectionMigrationFrom;

    /**
     * HBase连接对象（迁移To）
     */
    @Autowired
    @Qualifier("bigTableConnectionMigrationTo")
    private Connection bigTableConnectionMigrationTo;

    @Override
    public void run(String... args) throws Exception {
        // 记录日志
        log.info("Data migration config, TableFrom=(name={}, zk={}), TableTo=(name={}, zk={}), vinList={}",
                dataMigrationConfig.getTableFrom().getName(), dataMigrationConfig.getTableFrom().getZookeeper().getQuorum(),
                dataMigrationConfig.getTableTo().getName(), dataMigrationConfig.getTableTo().getZookeeper().getQuorum(),
                dataMigrationConfig.getVinList());

        // **开始作业**
        log.info("Data migration starting...");

        // **作业内容**
        String tableNameFrom = dataMigrationConfig.getTableFrom().getName();
        String tableNameTo = dataMigrationConfig.getTableTo().getName();
        List<String> vinList = dataMigrationConfig.getVinList();
        if (StringUtils.isNotBlank(tableNameFrom) && StringUtils.isNotBlank(tableNameTo) && null != vinList && 0 != vinList.size()) {
            // 按照vin集合顺序迁移数据
            vinList.forEach(vin -> {
                // 记录调试日志
                log.info("Data migration vin -> {}", vin);

                // 迁移主体业务
                String startKey = null;
                List<BigTableService.DataOrigin> dataOriginList;
                while (true) {
                    // 查询数据
                    dataOriginList = bigTableService.queryRecord(bigTableConnectionMigrationFrom, tableNameFrom,
                            vin, IBigTable.Sort.DESC, 100, startKey);

                    // 判断查询记录是否为空
                    if (null == dataOriginList || 0 == dataOriginList.size()) {
                        // 跳出循环
                        break;
                    } else {
                        // 执行迁移操作
                        dataOriginList.forEach(dataOrigin -> bigTableService.saveRecord(bigTableConnectionMigrationTo, tableNameTo, dataOrigin));

                        // 下一页
                        startKey = dataOriginList.get(dataOriginList.size() - 1).getRowKey();
                    }
                }
            });
        }

        // **完成作业**
        log.info("Data migration finished.");
    }
}
