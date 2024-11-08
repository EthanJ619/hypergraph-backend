package com.cqupt.medical.hypergraph.service;

/**
 * @Author EthanJ
 * @Date 2023/6/23 23:21
 * @Version 1.0
 */
public interface GraphService {

    String drawSpatialHg(String tableName, String taskName);

    String drawFactorHg(String tableName, String taskName, String algorithm, Object algorParams);

}
