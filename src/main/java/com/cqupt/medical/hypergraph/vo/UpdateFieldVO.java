package com.cqupt.medical.hypergraph.vo;


import lombok.Data;

import java.util.List;

@Data
public class UpdateFieldVO {

    // 疾病名称
    private String diseaseName;

    // 字段名字列表
    private List<String> fields;
}
