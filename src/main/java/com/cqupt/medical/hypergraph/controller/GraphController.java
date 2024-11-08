package com.cqupt.medical.hypergraph.controller;

import com.cqupt.medical.hypergraph.service.GraphService;
import com.cqupt.medical.hypergraph.service.TaskService;
import com.cqupt.medical.hypergraph.vo.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author EthanJ
 * @Date 2024/6/29 6:02
 * @Version 1.0
 */
@RestController
@RequestMapping("/graph")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/spatialHg")
    public String drawSpatialHg(@RequestBody TaskRequest taskRequest) {
        System.out.println(taskRequest);
        taskService.addTask(taskRequest);
        return graphService.drawSpatialHg(taskRequest.getDataset(), taskRequest.getTaskName());
    }

    @PostMapping("/factorHg")
    public String drawFactorHg(@RequestBody TaskRequest taskRequest, @RequestParam("algorithm") String algorithm) {
        System.out.println(taskRequest);
        taskService.addTask(taskRequest);
        return graphService.drawFactorHg(taskRequest.getDataset(), taskRequest.getTaskName(), algorithm, taskRequest.getAlgorParams());
    }
}
