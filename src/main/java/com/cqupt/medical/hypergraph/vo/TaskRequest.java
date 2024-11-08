package com.cqupt.medical.hypergraph.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    private String taskName;
    private String leader;
    private String participant;
    private String disease;
    private String model;
    private String remark;
    private double time;
    private String dataset;

    private String[] feature;
    private String[] para;
    private String[] paraValue;
    private Object algorParams;

    private String[] targetcolumn;
    private Integer uid;
}
