package com.cqupt.medical.hypergraph.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("task")
public class Task {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer taskId;
    private String taskName;
    private String taskType;
    private Timestamp createTime;

    private String creater;
    private Integer tableId;
}
