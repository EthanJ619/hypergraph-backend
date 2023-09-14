package com.cqupt.medical.hypergraph.service;

import com.cqupt.medical.hypergraph.util.JsonUtil;

/**
 * @Author EthanJ
 * @Date 2023/6/23 23:21
 * @Version 1.0
 */
public interface HGService {

    JsonUtil drawSpatialHG(String tableName, String taskName);

    JsonUtil drawTemporalHG(String tableName, String taskName);

    JsonUtil drawTSHG(String tableName, String taskName);
}
