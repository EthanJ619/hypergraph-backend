package com.cqupt.medical.hypergraph.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.medical.hypergraph.entity.Task;
import com.cqupt.medical.hypergraph.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/23 9:16
 * @Version 1.0
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> queryAllTasks() {
        return taskService.list();
    }

    @GetMapping("/task/{id}")
    public Task queryById(@PathVariable("id") Integer id) {
        return taskService.getById(id);
    }

    @GetMapping("/tasks/type/{type}")
    public List<Task> queryTaskByType(@PathVariable("type")String type) {
        return taskService.list(new QueryWrapper<Task>().eq("type", type));
    }

    @PostMapping("/task")
    public String createTask(@RequestBody Task task){
        taskService.save(task);
        return "create task log success!";
    }

    @DeleteMapping("/task/delete/{id}")
    public String delTask(@PathVariable("id") Integer id){
        taskService.removeById(id);
        return "delete task log success!";
    }

}
