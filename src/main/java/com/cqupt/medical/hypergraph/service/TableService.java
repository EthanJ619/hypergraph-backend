package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.Table;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author EthanJ
 * @Date 2023/6/23 17:02
 * @Version 1.0
 */
public interface TableService extends IService<Table> {

    void storeTable(MultipartFile tableFile, Table table);
}
