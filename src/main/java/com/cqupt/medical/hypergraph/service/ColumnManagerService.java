package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.ColumnManager;
import com.cqupt.medical.hypergraph.entity.dto.ColumnManagerDTO;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:04
 * @Version 1.0
 */
public interface ColumnManagerService extends IService<ColumnManager> {

//    List<ColumnManager> getAllData();

//    List<String> getFieldByTableName(String tableName);
//
//    List<ColumnManager> getAllColumnManagerByTableName(List<String> tableName);

    void insertField(ColumnManagerDTO columnManagerDTO);

    void delFieldByTableName(String tableName);
}
