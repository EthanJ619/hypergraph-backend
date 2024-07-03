package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.FieldEntity;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:04
 * @Version 1.0
 */
public interface FieldManageService extends IService<FieldEntity> {

    List<FieldEntity> getFieldByDiseaseName(String diseaseName);

    void updateFieldsByDiseaseName(String diseaseName, List<String> fields);
}
