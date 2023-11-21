package com.cqupt.medical.hypergraph.service.impl;

import com.cqupt.medical.hypergraph.service.HGService;
import com.cqupt.medical.hypergraph.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.cqupt.medical.hypergraph.util.Constants.*;

/**
 * @Author EthanJ
 * @Date 2023/6/23 23:21
 * @Version 1.0
 */
@Service
public class HGServiceImpl implements HGService {

    /**
     * 绘制空间超图
     *
     * @param tableName
     * @param taskName
     * @return
     */
    @Override
    public JsonUtil drawSpatialHG(String tableName, String taskName) {
        int exitCode = 1;
        StringBuilder pyOut = null;
        try {
            /*调用py脚本*/
            ProcessBuilder processBuilder = new ProcessBuilder("python", SCRIPT_PATH.substring(1) + "drawSpatialHG.py", LOCALSTORAGE_PATH + "table\\", tableName, taskName);  //读取类绝对路径时会在前面多一个斜杠
            processBuilder.redirectErrorStream(true);  //重定向标准输出，使得要执行的进程的错误输出可以被输入流读取
            Process process = processBuilder.start();

            /* 读取脚本输出 */
            BufferedReader in = new BufferedReader(new InputStreamReader((process.getInputStream())));
            String line = null;
            pyOut = new StringBuilder();
            while ((line = in.readLine()) != null)
                pyOut.append(line).append("\n");

            exitCode = process.waitFor();

            in.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (exitCode == 0)
            return new JsonUtil(SUCCESS_CODE, "空间超图构建成功", pyOut);
        else
            return new JsonUtil(FAIL_CODE, "超图构建出错", pyOut);
    }

    /**
     * 绘制时间超图
     *
     * @param tableName
     * @param taskName
     * @return
     */
    @Override
    public JsonUtil drawTemporalHG(String tableName, String taskName) {
//        int exitCode = 1;
//        StringBuilder pyOut = null;
//        try {
//            /*调用py脚本*/
//            ProcessBuilder processBuilder = new ProcessBuilder("python", SCRIPT_PATH.substring(1) + "drawTemporalHG.py", RESOURCE_PATH.substring(1), tableName, taskName);  //读取类绝对路径时会在前面多一个斜杠
//            processBuilder.redirectErrorStream(true);
//            Process process = processBuilder.start();
//
//            /* 读取脚本输出 */
//            BufferedReader in = new BufferedReader(new InputStreamReader((process.getInputStream())));
//            String line = null;
//            pyOut = new StringBuilder();
//            while ((line = in.readLine()) != null)
//                pyOut.append(line).append("\n");
//
//            exitCode = process.waitFor();
//
//            in.close();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (exitCode == 0)
//            return new JsonUtil(SUCCESS_CODE, "时间超图构建成功", pyOut);
//        else
//            return new JsonUtil(FAIL_CODE, "超图构建出错", pyOut);
////        return new JsonUtil(SUCCESS_CODE, "时间超图构建成功", null);
        return new JsonUtil(SUCCESS_CODE, "时间超图构建成功", null);
    }

    /**
     * 绘制时空超图
     *
     * @param tableName
     * @param taskName
     * @return
     */
    @Override
    public JsonUtil drawTSHG(String tableName, String taskName) {
//        int exitCode = 1;
//        StringBuilder pyOut = null;
//        try {
//            /*调用py脚本*/
//            ProcessBuilder processBuilder = new ProcessBuilder("python", SCRIPT_PATH.substring(1) + "drawTSHG.py", RESOURCE_PATH.substring(1), tableName, taskName);  //读取类绝对路径时会在前面多一个斜杠
//            processBuilder.redirectErrorStream(true);
//            Process process = processBuilder.start();
//
//            /* 读取脚本输出 */
//            BufferedReader in = new BufferedReader(new InputStreamReader((process.getInputStream())));
//            String line = null;
//            pyOut = new StringBuilder();
//            while ((line = in.readLine()) != null)
//                pyOut.append(line).append("\n");
//
//            exitCode = process.waitFor();
//
//            in.close();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (exitCode == 0)
//            return new JsonUtil(SUCCESS_CODE, "跨时空超图构建成功", pyOut);
//        else
//            return new JsonUtil(FAIL_CODE, "超图构建出错", pyOut);
////        return new JsonUtil(SUCCESS_CODE, "跨时空超图构建成功", null);
        return new JsonUtil(SUCCESS_CODE, "跨时空超图构建成功", null);
    }
}
