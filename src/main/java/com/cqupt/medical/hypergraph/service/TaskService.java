package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.AlgorithmUsageDailyStats;
import com.cqupt.medical.hypergraph.entity.Task;
import com.cqupt.medical.hypergraph.vo.TaskRequest;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/23 17:14
 * @Version 1.0
 */
public interface TaskService extends IService<Task> {

    List<AlgorithmUsageDailyStats> getAlgorithmUsageDailyStatsLast7Days();

    List<String> getAlgorithmName();

    List<Task> getTaskList();

    Task getlistbyId(Integer id);

    void deleteTask(int id);

    boolean addTask(TaskRequest taskRequest);
}
