package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.Table;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/23 17:02
 * @Version 1.0
 */
public interface TableService extends IService<Table> {

    //    String storeTable(MultipartFile tableFile, Table tableInfo);
    String storeTable(MultipartFile tableFile);

    List<Table> getAllTables();

    String deleteTable(String tableName);

    String saveTableInfo(Table tableInfo);

    String queryTableFeatures(String tableName);
}