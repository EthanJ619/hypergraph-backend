package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.FieldEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:39
 * @Version 1.0
 */
public interface FieldManageMapper extends BaseMapper<FieldEntity> {

    List<FieldEntity> getFieldByDiseaseName(@Param("diseaseName") String diseaseName);

    void updateFieldsByDiseaseName(@Param("diseaseName") String diseaseName, @Param("fields") List<String> fields);
}
