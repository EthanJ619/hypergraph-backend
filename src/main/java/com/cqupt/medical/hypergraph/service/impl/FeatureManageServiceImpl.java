package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.FeatureEntity;
import com.cqupt.medical.hypergraph.mapper.FeatureMapper;
import com.cqupt.medical.hypergraph.service.FeatureManageService;
import com.cqupt.medical.hypergraph.vo.FeatureClassVo;
import com.cqupt.medical.hypergraph.vo.FeatureListVo;
import com.cqupt.medical.hypergraph.vo.FeatureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author EthanJ
 * @Date 2024/3/30 22:15
 * @Version 1.0
 */
@Service
public class FeatureManageServiceImpl extends ServiceImpl<FeatureMapper, FeatureEntity> implements FeatureManageService {
    @Autowired
    FeatureMapper featureMapper;

    @Override
    public List<FeatureVo> getFeatureList(String belongType) {
        List<FeatureEntity> featureEntities = featureMapper.selectFeatures(belongType);
        //封装vo
        ArrayList<FeatureVo> featureVos = new ArrayList<>();
        for (FeatureEntity featureEntity : featureEntities) {
            FeatureVo featurevVo = new FeatureVo();
            featurevVo.setCharacterId(featureEntity.getCharacterId());
            featurevVo.setChName(featureEntity.getChName());
            featurevVo.setFeatureName(featureEntity.getFeatureName());
            featurevVo.setUnit(featureEntity.getUnit());
            //TODO 封装字段是否离散 离散就是discrete  连续就是continuous

            //TODO 如果离散就封装取值 连续就是封装范围
            featureVos.add(featurevVo);
        }
        return featureVos;
    }

    @Override
    public void insertFeatures(FeatureListVo featureListVo) {
        // 封装对象信息
        List<FeatureClassVo> tableHeaders = featureListVo.getTableHeaders();
        ArrayList<FeatureEntity> featureEntities = new ArrayList<>();
        for (FeatureClassVo tableHeader : tableHeaders) {
            FeatureEntity featureEntity = new FeatureEntity();
            featureEntity.setChName(null);// TODO 特征中文解释
            featureEntity.setFeatureName(tableHeader.getFieldName());
            featureEntity.setUnit("character varying"); // TODO  特征类型
            featureEntity.setDiseaseStandard(false);
            if (tableHeader.getIsDiagnosis() == "1") featureEntity.setPopulation(true);
            if (tableHeader.getIsPathology() == "1") featureEntity.setPhysiology(true);
            if (tableHeader.getIsExamine() == "1") featureEntity.setExamine(true);
            if (tableHeader.getIsLabel() == "1") featureEntity.setLabel(true);
            if (tableHeader.getIsVitalSigns() == "1") featureEntity.setSociety(true);
            featureEntities.add(featureEntity);
        }
        // TODO 作废方法，不需要完成
    }
}
