package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.ColumnManager;
import com.cqupt.medical.hypergraph.entity.dto.ColumnManagerDTO;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:39
 * @Version 1.0
 */
public interface ColumnManagerMapper extends BaseMapper<ColumnManager> {

    void insertColumn(ColumnManagerDTO columnManagerDTO);

//    List<String> getFieldByTableName(String tableName);

    void delColumnByTableName(String tableName);
}
