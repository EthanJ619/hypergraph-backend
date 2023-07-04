package com.cqupt.medical.hypergraph.util;

import java.io.IOException;
import java.util.Map;

/**
 * @Author EthanJ
 * @Date 2023/6/24 2:25
 * @Version 1.0
 */
public class PyTest {

    private static final String PATH = "D:/EthanJ/coding/workspace/PyCharm/HGTest/main.py";

    public static Map computePhoneAudioQuality() {
        String command = "python " + PATH;
        Process process;
//        Map map = new HashMap();
//        int i = 0;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
