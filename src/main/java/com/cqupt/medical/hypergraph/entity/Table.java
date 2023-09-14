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
 * @Date 2023/6/15 9:13
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("data_table")
public class Table {
    @TableId(value = "id", type = IdType.AUTO)
    private Long tableId;
    private String tableName;
    private String disease;
    private String tableSize;
    private Timestamp uploadTime;
    private String tableDesc;

    private String uploader;

    public Table(String tableName, String tableSize) {
        this.tableName = tableName;
        this.tableSize = tableSize;
    }
}
