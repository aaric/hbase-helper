package com.incarcloud.helper.runner;

import com.incarcloud.boar.bigtable.IBigTable;
import com.incarcloud.helper.config.DataDeletionConfig;
import com.incarcloud.helper.service.BigTableService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

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

        // **开始作业**
        log.info("Data deletion starting...");

        // **作业内容**
        if (StringUtils.isNotBlank(dataDeletionConfig.getTable())
                && null != dataDeletionConfig.getVinList()
                && 0 != dataDeletionConfig.getVinList().size()) {
            // 按照vin集合顺序删除数据
            dataDeletionConfig.getVinList().forEach(vin -> {
                // 记录调试日志
                log.info("Data deletion vin -> {}", vin);

                // 业务代码
                String tableName = dataDeletionConfig.getTable();
                List<BigTableService.DataOrigin> dataOriginList;
                while (true) {
                    // 查询数据
                    dataOriginList = bigTableService.queryRecord(tableName,
                            vin, IBigTable.Sort.DESC, 100, null);

                    // 判断查询记录是否为空
                    if (null == dataOriginList || 0 == dataOriginList.size()) {
                        // 跳出循环
                        break;
                    } else {
                        // 执行删除操作
                        dataOriginList.forEach(dataOrigin -> bigTableService.deleteRecord(tableName, dataOrigin.getRowKey()));
                    }
                }
            });
        }

        // **完成作业**
        log.info("Data deletion finished.");
    }
}
