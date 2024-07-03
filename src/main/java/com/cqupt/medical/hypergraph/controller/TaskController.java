package com.cqupt.medical.hypergraph.controller;

import com.cqupt.medical.hypergraph.entity.Task;
import com.cqupt.medical.hypergraph.service.TaskService;
import com.cqupt.medical.hypergraph.util.Result;
import com.cqupt.medical.hypergraph.util.TaskRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author EthanJ
 * @Date 2023/6/23 9:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/Task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/all")
    public Result getTaskList() {
        return Result.success(taskService.getTaskList());
    }

    @GetMapping("/result/{id}")
    public Result result(@PathVariable int id) throws JsonProcessingException {
        Task task = taskService.getlistbyId(id);
        TaskRequest request = new TaskRequest();
        System.out.println(task);
        ObjectMapper objectMapper = new ObjectMapper();
        String res = task.getResult();
        String[][] retrievedArray = objectMapper.readValue(res, String[][].class);

        String fea1 = task.getFeature();
        String[] fea = fea1.split(",");

        String tar1 = task.getTargetcolumn();
        String[] tar = tar1.split(",");

        String para1 = task.getParameters();
        String[] para = new String[0];
        if (para1 != null) {
            para = para1.split(",");
        }

        String paraV1 = task.getParametersvalues();
        String[] paraV = new String[0];
        if (paraV1 != null) {
            paraV = paraV1.split(",");
        }

        request.setCi(task.getCi());
        request.setRatio(String.valueOf(task.getRatio()));
        request.setRes(retrievedArray);
        request.setTime(task.getUsetime());
        request.setDisease(task.getDisease());
        request.setDisease(task.getDisease());
        request.setFeature(fea);
        request.setLeader(task.getLeader());
        request.setModel(task.getModel());
        request.setPara(para);
        request.setParaValue(paraV);
        request.setParticipant(task.getParticipant());
        request.setTargetcolumn(tar);
        request.setTaskName(task.getTaskname());
        request.setDataset(task.getDataset());
        request.setUid(task.getUserid());
        return Result.success(request);

    }

    @GetMapping("/delete/{id}")
    public Result deleteById(@PathVariable int id) {
        taskService.deleteTask(id);
        return Result.success(taskService.getTaskList());
    }

}
