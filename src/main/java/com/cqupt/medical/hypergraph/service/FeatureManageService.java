package com.cqupt.medical.hypergraph.service;

import com.cqupt.medical.hypergraph.vo.FeatureListVo;
import com.cqupt.medical.hypergraph.vo.FeatureVo;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2024/3/30 22:14
 * @Version 1.0
 */
public interface FeatureManageService {
    List<FeatureVo> getFeatureList(String belongType);

    void insertFeatures(FeatureListVo featureListVo);
}
