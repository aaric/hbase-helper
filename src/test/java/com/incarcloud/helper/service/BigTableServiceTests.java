package com.incarcloud.helper.service;

import com.incarcloud.boar.bigtable.IBigTable;
import com.incarcloud.boar.datapack.DataPackOverview;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * BigTableServiceTests
 *
 * @author Aaric, created on 2020-07-13T09:27.
 * @version 0.1.0-SNAPSHOT
 */
@Log4j2
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BigTableServiceTests {

    @Autowired
    private BigTableService bigTableService;

    @Test
    @Disabled
    public void testGetRecord() {
        BigTableService.DataOrigin dataOrigin = bigTableService.getRecord("zs:telemetry_jtt808",
                "0187000TESTGPS0000000002JTT808POSITION#20200710103958####0001");
        log.debug("dataOrigin --> {}", dataOrigin);
        Assertions.assertNotNull(dataOrigin);
    }

    @Test
    @Disabled
    public void testDeleteRecord() {
        Assertions.assertTrue(bigTableService.deleteRecord("zs:telemetry_ic",
                "e498000TESTBOX0000000001CHECK##########20200713153656####0001"));
    }

    @Test
    @Disabled
    public void testQueryRecord() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateFormat.parse("2020-07-13 12:00:00");
        Date endTime = dateFormat.parse("2020-07-14 12:00:00");
        List<BigTableService.DataOrigin> dataOriginList = bigTableService.queryRecord("zs:telemetry_ic",
                "TESTBOX0000000001", DataPackOverview.class, IBigTable.Sort.DESC, startTime, endTime, 50, null);
        dataOriginList.forEach(o -> System.out.println(o));
        Assertions.assertNotNull(dataOriginList);
    }
}
