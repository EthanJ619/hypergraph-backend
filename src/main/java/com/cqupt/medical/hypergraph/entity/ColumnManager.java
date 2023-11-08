package com.cqupt.medical.hypergraph.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author EthanJ
 * @Date 2023/10/13 20:34
 * @Version 1.0
 */
@TableName(value = "t_column_manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnManager implements Serializable {
    private Integer id;

    private String tableName;

    private String tableNameCh;

    private String fieldName;

    private String fieldNameCh;

    private String fieldDesc;

    private String fieldType;

    private String fieldRange;

    private String fieldUnit;

    private String diseaseType;

    private String isDemography;

    private String isPhysiological;

    private String isSociology;

    private String isClinicalRelationship;

    private String isMultipleDiseases;

    private String isZooInformation;

    private String isQuestionnaire;

    private String isTimeInformation;

    private String startTime;

    private String endTime;

    private String timeSpace;

    private String createTime;

    private String updateTime;

    private String tablePeople;

    private String tableOrigin;

    private String tableSize;

    private String tableStatus;

    private static final long serialVersionUID = 1L;
}
