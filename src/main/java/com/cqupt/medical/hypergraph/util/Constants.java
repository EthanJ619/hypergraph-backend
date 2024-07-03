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

    public static final String RESOURCE_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static" + File.separator;
    public static final String WORKSPACE_PATH = System.getProperty("user.dir") + File.separator;
    public static final String LOCALSTORAGE_PATH = WORKSPACE_PATH + "software6" + File.separator;
    //    public static final String LOCALSTORAGE_PATH=WORKSPACE_PATH + "hypergraph" + File.separator;
    public static final String SCRIPT_PATH = RESOURCE_PATH + "script" + File.separator;

    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "500";

    public static String getLocalStoragePath() {
        return LOCALSTORAGE_PATH.replace('\\', '/');
    }
}
