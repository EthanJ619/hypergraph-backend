package com.cqupt.medical.hypergraph.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("software6log")
@Data
public class LogEntity {
    @TableId
    private Integer id;

    private String uid;
    private String username;
    private String operation;
    private String opTime;
    private Integer role;

    private static final long serialVersionUID = 1L;
}

