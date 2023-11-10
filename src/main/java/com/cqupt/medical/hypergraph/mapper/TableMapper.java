package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.Table;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/23 10:23
 * @Version 1.0
 */
public interface TableMapper extends BaseMapper<Table> {

    void createTable(@Param("tableName") String tableName, @Param("tableHeaders") LinkedHashMap<String, String> tableHeaders);

    void insertData(@Param("tableName") String tableName, @Param("columns") List<String> columns, @Param("rows") List<String[]> rows);

    void delTable(String tableName);
}
