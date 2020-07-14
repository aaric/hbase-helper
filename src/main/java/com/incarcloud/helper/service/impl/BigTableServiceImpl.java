package com.incarcloud.helper.service.impl;

import com.incarcloud.boar.bigtable.IBigTable;
import com.incarcloud.boar.datapack.DataPackObject;
import com.incarcloud.boar.util.RowKeyUtil;
import com.incarcloud.helper.service.BigTableService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 大数据服务接口实现
 *
 * @author Aaric, created on 2020-07-10T16:29.
 * @version 0.1.0-SNAPSHOT
 */
@Log4j2
@Service
public class BigTableServiceImpl implements BigTableService {

    /**
     * HBase连接对象
     */
    @Autowired
    private Connection bigTableConnection;

    @Override
    public boolean saveRecord(String tableName, String rowKey, DataOrigin dataOrigin) {
        // 存储到大数据
        try (Table table = bigTableConnection.getTable(TableName.valueOf(tableName))) {
            // base-族，data-解析数据，origin-原始报文
            Put put = new Put(rowKey.getBytes());
            put.addColumn(Bytes.toBytes(FAMILY_BASE), Bytes.toBytes(QUALIFIER_DATA), Bytes.toBytes(dataOrigin.getDataString())); //解析数据
            put.addColumn(Bytes.toBytes(FAMILY_BASE), Bytes.toBytes(QUALIFIER_ORIGIN), Bytes.toBytes(dataOrigin.getOriginString())); //原始报文数据

            // 执行put操作
            table.put(put);

            // 打印日志
            log.debug("Save rowKey: {}", rowKey);

            // 返回结果
            return true;
        } catch (IOException e) {
            log.error("save data exception", e);
        }
        return false;
    }

    @Override
    public DataOrigin getRecord(String tableName, String rowKey) {
        // 根据row key查询记录
        try (Table table = bigTableConnection.getTable(TableName.valueOf(tableName))) {
            // 读取数据
            Result result = table.get(new Get(Bytes.toBytes(rowKey)));

            // 解析数据
            Cell dataCell = result.getColumnLatestCell(Bytes.toBytes(FAMILY_BASE), Bytes.toBytes(QUALIFIER_DATA));
            String dataString = Bytes.toString(CellUtil.cloneValue(dataCell));
            long dataTs = dataCell.getTimestamp(); //入库时间

            // 原始报文数据
            Cell originCell = result.getColumnLatestCell(Bytes.toBytes(FAMILY_BASE), Bytes.toBytes(QUALIFIER_ORIGIN));
            String originString = Bytes.toString(CellUtil.cloneValue(originCell));
            long originTs = originCell.getTimestamp(); //入库时间

            // 判断json字符串是否为空白字符
            if (StringUtils.isNotBlank(dataString)) {
                // 返回结果
                return new DataOrigin(dataString, dataTs, originString, originTs);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteRecord(String tableName, String rowKey) {
        // 根据row key删除记录
        try (Table table = bigTableConnection.getTable(TableName.valueOf(tableName))) {
            // 删除数据
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);

            // 返回成功
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public <T extends DataPackObject> List<DataOrigin> queryRecord(String tableName, String vin, Class<T> clazz, IBigTable.Sort sort, Date startTime, Date endTime, Integer pageSize, String startKey) {
        // 分页查询记录
        try (Table table = bigTableConnection.getTable(TableName.valueOf(tableName))) {
            // 构建查询条件
            Scan scan = new Scan();

            // 计算start和stop键值
            String startRowKey = startKey;
            String stopRowKey;
            if (null == sort || IBigTable.Sort.DESC == sort) {
                // 如果不传startKey，按照时间倒序查询
                if (StringUtils.isBlank(startKey)) {
                    // 判断是否设置了查询结束时间
                    if (null == endTime) {
                        // 查询范围比较大
                        startRowKey = RowKeyUtil.makeMaxRowKey(vin, clazz);
                    } else {
                        // 查询范围比较小
                        startRowKey = RowKeyUtil.makeMaxRowKey(vin, clazz, endTime);
                    }
                }

                // 判断是否设置了查询开始时间
                if (null == startTime) {
                    // 查询范围比较大
                    stopRowKey = RowKeyUtil.makeMinRowKey(vin, clazz);
                } else {
                    // 查询范围比较小
                    stopRowKey = RowKeyUtil.makeMinRowKey(vin, clazz, startTime);
                }

                // 按照时间倒序
                scan.setReversed(true);
            } else {
                // 如果不传startKey，按照时间升序查询
                if (StringUtils.isBlank(startKey)) {
                    // 判断是否设置了查询开始时间
                    if (null == startTime) {
                        // 查询范围比较大
                        startRowKey = RowKeyUtil.makeMinRowKey(vin, clazz);
                    } else {
                        // 查询范围比较小
                        startRowKey = RowKeyUtil.makeMinRowKey(vin, clazz, startTime);
                    }
                }

                // 判断是否设置了查询结束时间
                if (null == endTime) {
                    // 查询范围比较大
                    stopRowKey = RowKeyUtil.makeMaxRowKey(vin, clazz);
                } else {
                    // 查询范围比较小
                    stopRowKey = RowKeyUtil.makeMaxRowKey(vin, clazz, endTime);
                }
            }

            // String转Bytes
            byte[] startRowBytes = Bytes.toBytes(startRowKey);
            startRowBytes = Bytes.copy(startRowBytes, 0, startRowBytes.length - 1); //包含关系
            byte[] stopRowBytes = Bytes.toBytes(stopRowKey);

            // 设置查询数据范围
            scan.setStartRow(startRowBytes);
            scan.setStopRow(stopRowBytes);

            // 构建过滤器
            FilterList filterList = new FilterList();
            filterList.addFilter(new SkipFilter(new SingleColumnValueFilter(Bytes.toBytes(FAMILY_BASE),
                    Bytes.toBytes(QUALIFIER_HIDDEN),
                    CompareFilter.CompareOp.EQUAL,
                    new NullComparator()))); //单列值过滤器
            filterList.addFilter(new PageFilter(pageSize)); //分页过滤器

            // 设置过滤器
            scan.setFilter(filterList);

            // 遍历查询结果集
            T data;
            List<DataOrigin> dataList = new ArrayList<>();
            ResultScanner rs = table.getScanner(scan);
            Cell dataCell;
            String dataString;
            Cell originCell;
            String originString;
            for (Result result : rs) {
                // 解析数据
                dataCell = result.getColumnLatestCell(Bytes.toBytes(FAMILY_BASE), Bytes.toBytes(QUALIFIER_DATA));
                dataString = Bytes.toString(CellUtil.cloneValue(dataCell));
                long dataTs = dataCell.getTimestamp(); //入库时间

                // 原始报文数据
                originCell = result.getColumnLatestCell(Bytes.toBytes(FAMILY_BASE), Bytes.toBytes(QUALIFIER_ORIGIN));
                originString = Bytes.toString(CellUtil.cloneValue(originCell));
                long originTs = originCell.getTimestamp(); //入库时间

                if (StringUtils.isNotBlank(dataString)) {
                    try {
                        // 添加返回值
                        dataList.add(new DataOrigin(dataString, dataTs, originString, originTs));
                    } catch (Exception e) {
                        log.error("queryData: json convert object exception", e);
                    }
                }
            }
            rs.close();

            // 返回数据集
            return dataList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
