package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.Task;
import com.cqupt.medical.hypergraph.mapper.TaskMapper;
import com.cqupt.medical.hypergraph.service.HGService;
import com.cqupt.medical.hypergraph.service.TaskService;
import com.cqupt.medical.hypergraph.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.util.Objects;

import static com.cqupt.medical.hypergraph.util.Constants.*;

/**
 * @Author EthanJ
 * @Date 2023/6/23 19:08
 * @Version 1.0
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private HGService hgService;

    @Override
    @Transactional
    public String createTask(Task task) {
        if (getOne(new QueryWrapper<Task>().eq("task_name", task.getTaskName())) != null)
            return new JsonUtil(FAIL_CODE, "任务名称重复！", null).toJsonString();

        /* 创建存放超图构建结果的文件夹 */
        File plotFolder = new File(LOCALSTORAGE_PATH + "plot\\");
        if (!plotFolder.exists())
            plotFolder.mkdir();

        /*存放任务信息*/
        try {
            save(task);
        } catch (TransactionException e) {
            e.printStackTrace();
            return new JsonUtil(FAIL_CODE, "创建任务失败", null).toJsonString();
        }

        /*绘制超图*/
        JsonUtil hgMsg = new JsonUtil();
        if (Objects.equals(task.getTaskType(), "空间超图"))
            hgMsg = hgService.drawSpatialHG(task.getTableName(), task.getTaskName());
        else if (Objects.equals(task.getTaskType(), "时间超图"))
            hgMsg = hgService.drawTemporalHG(task.getTableName(), task.getTaskName());
        else if (Objects.equals(task.getTaskType(), "跨时空超图"))
            hgMsg = hgService.drawTSHG(task.getTableName(), task.getTaskName());

        /* 如果绘制出错就回滚 */
        if (Objects.equals(hgMsg.getCode(), FAIL_CODE)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonUtil(FAIL_CODE, "超图构建失败", hgMsg.getData()).toJsonString();
        } else
            return new JsonUtil(SUCCESS_CODE, "超图构建成功", hgMsg.getData()).toJsonString();
    }

    /**
     * 删除任务日志文件
     *
     * @param taskName
     * @return
     */
    @Override
    public String delRecordFile(String taskName) {
        String folderPath = LOCALSTORAGE_PATH + "plot" + File.separator + taskName;
        File plotFolder = new File(folderPath);
        if (plotFolder.exists()) {
            if (deleteFolder(plotFolder)) {
                remove(new QueryWrapper<Task>().eq("task_name", taskName));  //删除数据库中表项
                return new JsonUtil(SUCCESS_CODE, "删除成功", null).toJsonString();
            } else return new JsonUtil(FAIL_CODE, "任务记录文件删除失败", null).toJsonString();
        } else
            return new JsonUtil(FAIL_CODE, "任务记录文件不存在", null).toJsonString();
    }

    /**
     * 删除包含文件的文件夹
     *
     * @param folder
     */
    private boolean deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        return folder.delete();
    }
}
