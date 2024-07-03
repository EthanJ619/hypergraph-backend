package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.FieldEntity;
import com.cqupt.medical.hypergraph.entity.FieldManagement;
import com.cqupt.medical.hypergraph.mapper.FieldManageMapper;
import com.cqupt.medical.hypergraph.service.FieldManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:04
 * @Version 1.0
 */
@Service
public class FieldManageServiceImpl extends ServiceImpl<FieldManageMapper, FieldEntity> implements FieldManageService {

    @Autowired
    private FieldManageMapper fieldManageMapper;

    @Override
    public List<FieldEntity> getFieldByDiseaseName(String diseaseName) {

        List<FieldEntity> res = fieldManageMapper.getFieldByDiseaseName(diseaseName);

        return res;
    }

    @Override
    public void updateFieldsByDiseaseName(String diseaseName, List<String> fields) {

        fieldManageMapper.updateFieldsByDiseaseName(diseaseName, fields);
    }
}
