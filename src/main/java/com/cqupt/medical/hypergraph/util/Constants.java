package com.cqupt.medical.hypergraph.util;

import org.springframework.util.ClassUtils;

import java.io.File;

/**
 * 常量类
 *
 * @Author EthanJ
 * @Date 2023/8/5 11:08
 * @Version 1.0
 */
public class Constants {

    public static final String RESOURCE_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath().replace("/", "\\") + "static" + File.separator;
    public static final String WORKSPACE_PATH = System.getProperty("user.dir");
    public static final String LOCALSTORAGE_PATH=".\\software6\\";
//    public static final String LOCALSTORAGE_PATH=".\\hypergraph\\";
    public static final String SCRIPT_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath().replace("/", "\\") + "static" + File.separator + "script" + File.separator;

    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "500";

}
