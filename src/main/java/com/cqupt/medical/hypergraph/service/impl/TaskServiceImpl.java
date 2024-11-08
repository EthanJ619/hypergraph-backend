package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.AlgorithmUsageDailyStats;
import com.cqupt.medical.hypergraph.entity.Task;
import com.cqupt.medical.hypergraph.mapper.TaskMapper;
import com.cqupt.medical.hypergraph.service.TaskService;
import com.cqupt.medical.hypergraph.vo.TaskRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/23 19:08
 * @Version 1.0
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Resource
    TaskMapper taskMapper;


    @Override
    public List<AlgorithmUsageDailyStats> getAlgorithmUsageDailyStatsLast7Days() {
        return taskMapper.getAlgorithmUsageDailyStatsLast7Days();
    }

    @Override
    public List<String> getAlgorithmName() {
        return taskMapper.getAlgorithmName();
    }

    @Override
    public List<Task> getTaskList() {
        return taskMapper.getTaskList();
    }

    @Override
    public Task getlistbyId(Integer id) {
        return taskMapper.getlistbyId(id);
    }

    @Override
    public void deleteTask(int id) {
        taskMapper.deleteTask(id);
    }

    @Override
    public boolean addTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTaskname(taskRequest.getTaskName());
        task.setDisease(taskRequest.getDisease());
        task.setDataset(taskRequest.getDataset());
        task.setLeader(taskRequest.getLeader());
        task.setParticipant(taskRequest.getParticipant());
        task.setModel(taskRequest.getModel());
        task.setRemark(taskRequest.getRemark());
        task.setUserid(taskRequest.getUid());

        String[] fea = taskRequest.getFeature();
        if (fea != null)
            task.setFeature(String.join(",", fea));

        String[] tarCol = taskRequest.getTargetcolumn();
        if (tarCol != null)
            task.setTargetcolumn(String.join(",", tarCol));

        String[] para = taskRequest.getPara();
        if (para != null)
            task.setParameters(String.join(",", para));

        String[] paraV = taskRequest.getParaValue();
        if (paraV != null)
            task.setParametersvalues(String.join(",", paraV));

        return save(task);
    }
}
