package com.cqupt.medical.hypergraph.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:15
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnManagerDTO {

    private String tableName;
    private List<ColumnDTO> tableHeaders;

}
