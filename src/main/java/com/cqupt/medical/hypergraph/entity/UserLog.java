package com.cqupt.medical.hypergraph.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName user_log
 */
@TableName(value ="user_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Date opTime;

    private String opType;

    private String username;

    private static final long serialVersionUID = 1L;
}