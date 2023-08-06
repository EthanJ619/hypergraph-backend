package com.cqupt.medical.hypergraph.util;

import org.springframework.util.ClassUtils;

/**
 * 常量类
 *
 * @Author EthanJ
 * @Date 2023/8/5 11:08
 * @Version 1.0
 */
public class Constants {

    public static final String TABLE_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/table/";

    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "500";

}
