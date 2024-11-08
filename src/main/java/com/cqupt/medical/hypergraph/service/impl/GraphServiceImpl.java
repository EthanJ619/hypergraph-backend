package com.cqupt.medical.hypergraph.service.impl;

import com.cqupt.medical.hypergraph.service.GraphService;
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
public class GraphServiceImpl implements GraphService {

    /**
     * 绘制空间超图
     *
     * @param tableName
     * @param taskName
     * @return
     */
    @Override
    public String drawSpatialHg(String tableName, String taskName) {
        int exitCode = 1;
        StringBuilder pyOut = null;
        try {
            /*调用py脚本*/
            ProcessBuilder processBuilder = new ProcessBuilder("python", SCRIPT_PATH.substring(1) + "drawSpatialHg.py", LOCALSTORAGE_PATH, tableName, taskName);  //读取类绝对路径时会在前面多一个斜杠
//            ProcessBuilder processBuilder = new ProcessBuilder("conda", "run", "-n", "software6", "python", "/home/data/WorkSpace/Backend/software6/algorithm/" + "drawSpatialHg.py", LOCALSTORAGE_PATH, tableName, taskName);  //绵阳部署
//            ProcessBuilder processBuilder = new ProcessBuilder("conda", "run", "-n", "software6", "python", "/root/keti3/backend/software6/script/" + "drawSpatialHg.py", LOCALSTORAGE_PATH, tableName, taskName);  //山东部署
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
            return new JsonUtil(SUCCESS_CODE, "空间超图构建成功", pyOut).toJsonString();
        else
            return new JsonUtil(FAIL_CODE, "超图构建出错", pyOut).toJsonString();
    }

    /**
     * 绘制时间超图
     *
     * @param tableName
     * @param taskName
     * @return
     */
    @Override
    public String drawFactorHg(String tableName, String taskName, String algorithm, Object algorParams) {
        int exitCode = 1;
        StringBuilder pyOut = null;
        try {
            /*调用py脚本*/
            ProcessBuilder processBuilder = new ProcessBuilder("python", SCRIPT_PATH.substring(1) + "drawFactorHg.py", tableName, algorithm, algorParams.toString());  //读取类绝对路径时会在前面多一个斜杠
//            ProcessBuilder processBuilder = new ProcessBuilder("conda", "run", "-n", "software6", "python", "/home/data/WorkSpace/Backend/software6/algorithm/" + "drawFactorHg.py", tableName, algorithm, algorParams.toString());  //绵阳部署
//            ProcessBuilder processBuilder = new ProcessBuilder("conda", "run", "-n", "software6", "python", "/root/keti3/backend/software6/script/" + "drawFactorHg.py", tableName, algorithm, algorParams.toString());  //山东部署
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            /* 读取脚本输出 */
            BufferedReader in = new BufferedReader(new InputStreamReader((process.getInputStream())));
            String line = null;
            pyOut = new StringBuilder();
            while ((line = in.readLine()) != null) {
                pyOut.append(line).append("\n"); //获得所有输出，用于后端调试
                if (line.startsWith("{\"edges\":")) {
                    pyOut.setLength(0);
                    pyOut.append(line).append("\n");
                    break;
                }
            }

            exitCode = process.waitFor();

            in.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (exitCode == 0)
            return new JsonUtil(SUCCESS_CODE, "危险因素超图构建成功", pyOut).toJsonString();
        else
            return new JsonUtil(FAIL_CODE, "超图构建出错", pyOut).toJsonString();
//        return new JsonUtil(SUCCESS_CODE, "时间超图构建成功", null).toJsonString();

    }
}
