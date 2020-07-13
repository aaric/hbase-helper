package com.incarcloud.helper.service;

import com.incarcloud.boar.bigtable.IBigTable;
import com.incarcloud.boar.datapack.DataPackObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
         * 解析数据
         */
        private String dataString;

        /**
         * 原始报文数据
         */
        private String originString;
    }

    /**
     * 存储解析数据
     *
     * @param tableName  表名称
     * @param rowKey     存储rowKey
     * @param dataOrigin 解析数据与原始报文数据
     * @return
     */
    boolean saveRecord(String tableName, String rowKey, DataOrigin dataOrigin);

    /**
     * 根据row key查询记录
     *
     * @param tableName 表名称
     * @param rowKey    主键
     * @return
     */
    DataOrigin getRecord(String tableName, String rowKey);

    /**
     * 根据row key删除记录
     *
     * @param tableName 表名称
     * @param rowKey    主键
     * @return
     */
    boolean deleteRecord(String tableName, String rowKey);

    /**
     * 分页查询记录
     *
     * @param tableName 表名称
     * @param vin       车架号
     * @param clazz     指定DataPack类型
     * @param sort      默认按照时间倒序，排序规则：按照时间升序或者倒序
     * @param startTime 查询开始时间，如果有startKey，startTime设置无效
     * @param endTime   查询结束时间
     * @param pageSize  分页大小
     * @param startKey  指定起始RowKey
     * @param <T>
     * @return
     */
    <T extends DataPackObject> List<T> queryData(String tableName, String vin, Class<T> clazz, IBigTable.Sort sort, Date startTime, Date endTime, Integer pageSize, String startKey);
}
