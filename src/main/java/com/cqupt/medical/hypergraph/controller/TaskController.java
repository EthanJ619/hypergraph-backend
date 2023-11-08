package com.cqupt.medical.hypergraph.controller;

import com.cqupt.medical.hypergraph.entity.Task;
import com.cqupt.medical.hypergraph.service.HGService;
import com.cqupt.medical.hypergraph.service.TaskService;
import com.cqupt.medical.hypergraph.util.JsonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cqupt.medical.hypergraph.util.Constants.RESOURCE_PATH;
import static com.cqupt.medical.hypergraph.util.Constants.SUCCESS_CODE;


/**
 * @Author EthanJ
 * @Date 2023/6/23 9:16
 * @Version 1.0
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HGService hgService;

    @GetMapping("/tasks")
    public JsonUtil<List<Task>> queryAllTasks(int pageNum) {
        PageHelper.startPage(pageNum, 15);
        List<Task> tasks = taskService.list();
        PageInfo<Task> taskPageInfo = new PageInfo<>(tasks);
        return new JsonUtil<>(SUCCESS_CODE, "获取任务日志成功", tasks, taskPageInfo.getPages());
    }

    @GetMapping("/task/{id}")
    public Task queryById(@PathVariable("id") Integer id) {
        return taskService.getById(id);
    }

    @PostMapping("/task")
    public String createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/task/delete/{task_name}")
    public String delTask(@PathVariable("task_name") String taskName) {
        return taskService.delRecordFile(taskName);
    }

    /**
     * 获取静态资源路径
     *
     * @return
     */
    @GetMapping("/resource")
    public String getResourceFolder() {
        return RESOURCE_PATH.substring(1);
    }
}
