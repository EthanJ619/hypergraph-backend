package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.Task;
import com.cqupt.medical.hypergraph.mapper.TaskMapper;
import com.cqupt.medical.hypergraph.service.TaskService;
import org.springframework.stereotype.Service;

/**
 * @Author EthanJ
 * @Date 2023/6/23 19:08
 * @Version 1.0
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
}
