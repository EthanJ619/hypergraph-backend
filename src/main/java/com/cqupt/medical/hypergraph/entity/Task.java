package com.cqupt.medical.hypergraph.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Author EthanJ
 * @Date 2023/6/23 9:16
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="task")
public class Task {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String taskname;
    private String leader;
    private String participant;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createtime;

    private String disease;
    private String model;
    private String remark;
    private String feature;
    private String parameters;
    private String parametersvalues;
    private String targetcolumn;
    private double usetime;
    private String dataset;

    private Integer userid;

}
