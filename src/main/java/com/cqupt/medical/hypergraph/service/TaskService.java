package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.Task;

/**
 * @Author EthanJ
 * @Date 2023/6/23 17:14
 * @Version 1.0
 */
public interface TaskService extends IService<Task> {

    String createTask(Task task);

    String delRecordFile(String taskName);

}
