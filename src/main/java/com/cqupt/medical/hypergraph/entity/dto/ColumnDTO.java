package com.cqupt.medical.hypergraph.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:17
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDTO {

    private String fieldName;
    private String isDemography;
    private String isPhysiological;
    private String isSociology;
    private String isLabel;
}
