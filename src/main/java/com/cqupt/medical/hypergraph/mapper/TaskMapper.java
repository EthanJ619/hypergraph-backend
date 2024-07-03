package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.AlgorithmUsageDailyStats;
import com.cqupt.medical.hypergraph.entity.Task;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/23 10:23
 * @Version 1.0
 */
public interface TaskMapper extends BaseMapper<Task> {
    List<AlgorithmUsageDailyStats> getAlgorithmUsageDailyStatsLast7Days();

    List<String> getAlgorithmName();

    List<Task> getTaskList();


    Task getlistbyId(Integer id);

    void deleteTask(int id);
}
