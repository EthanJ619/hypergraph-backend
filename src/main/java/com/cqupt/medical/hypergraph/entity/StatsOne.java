package com.cqupt.medical.hypergraph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsOne {
    // 统计信息1
    private Integer specialDiseaseNumber;
    private Integer itemNumber;
    private String startTime;
    private String endTime;
    private Integer taskNumber;

}
