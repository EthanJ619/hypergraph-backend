package com.cqupt.medical.hypergraph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmUsage {
    private String model;  // 算法名
    private int usageCount; // 使用次数
}
