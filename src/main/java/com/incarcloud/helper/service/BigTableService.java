package com.incarcloud.helper.service;

import com.incarcloud.boar.bigtable.IBigTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.hbase.client.Connection;

import java.util.List;

/**
 * 大数据服务接口
 *
 * @author Aaric, created on 2020-07-10T16:26.
 * @version 0.1.0-SNAPSHOT
 */
public interface BigTableService {

    /**
     * 族
     */
    String FAMILY_BASE = "base";

    /**
     * 列名-解析数据
     */
    String QUALIFIER_DATA = "data";

    /**
     * 列名-原始报文
     */
    String QUALIFIER_ORIGIN = "origin";

    /**
     * 列名-隐藏标记
     */
    String QUALIFIER_HIDDEN = "hidden";

    /**
     * 解析数据与原始报文数据
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DataOrigin {
        /**
         * 存储rowKey
         */
        private String rowKey;

        /**
         * 解析数据
         */
        private String dataString;

        /**
         * 解析数据时间戳
         */
        private long dataTs;

        /**
         * 原始报文数据
         */
        private String originString;

        /**
         * 原始报文数据时间戳
         */
        private long originTs;
    }

    /**
     * 存储解析数据
     *
     * @param bigTableConnection HBase连接对象
     * @param tableName          表名称
     * @param dataOrigin         解析数据与原始报文数据
     * @return
     */
    boolean saveRecord(Connection bigTableConnection, String tableName, DataOrigin dataOrigin);

    /**
     * 根据row key查询记录
     *
     * @param bigTableConnection HBase连接对象
     * @param tableName          表名称
     * @param rowKey             主键
     * @return
     */
    DataOrigin getRecord(Connection bigTableConnection, String tableName, String rowKey);

    /**
     * 根据row key删除记录
     *
     * @param bigTableConnection HBase连接对象
     * @param tableName          表名称
     * @param rowKey             主键
     * @return
     */
    boolean deleteRecord(Connection bigTableConnection, String tableName, String rowKey);

    /**
     * 分页查询记录
     *
     * @param bigTableConnection HBase连接对象
     * @param tableName          表名称
     * @param vin                车架号
     * @param sort               默认按照时间倒序，排序规则：按照时间升序或者倒序
     * @param pageSize           分页大小
     * @param startKey           指定起始RowKey
     * @return
     */
    List<DataOrigin> queryRecord(Connection bigTableConnection, String tableName, String vin, IBigTable.Sort sort, Integer pageSize, String startKey);
}
