package com.cqupt.medical.hypergraph.controller;

import com.cqupt.medical.hypergraph.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/spatialHg")
    public String drawSpatialHg(@RequestBody Map<String, String> taskInfo) {
        System.out.println(taskInfo);
        return graphService.drawSpatialHg(taskInfo.get("tableid"), taskInfo.get("tablename"), taskInfo.get("taskname"));
    }

    @PostMapping("/factorHg")
    public String drawFactorHg(@RequestBody Map<String, Object> taskInfo, @RequestParam("algorithm") String algorithm) {
        System.out.println(taskInfo);
        System.out.println(algorithm);
        return graphService.drawFactorHg(taskInfo.get("tableid").toString(), taskInfo.get("tablename").toString(), taskInfo.get("taskname").toString(), algorithm, taskInfo.get("algorparams"));
    }
}
