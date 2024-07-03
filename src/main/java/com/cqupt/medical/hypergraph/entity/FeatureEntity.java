package com.cqupt.medical.hypergraph.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

// TODO 公共模块新增类

@Data
@TableName("field_management")
public class FeatureEntity implements Serializable {
    @TableId
    private Integer characterId;

    private String featureName;
    private String chName;

    private boolean diseaseStandard = false;
    private boolean population = false;
    private boolean examine = false;
    private boolean physiology = false;
    private boolean society = false;
    private String tableName;
    private String unit;
    private boolean isLabel = false;

}
