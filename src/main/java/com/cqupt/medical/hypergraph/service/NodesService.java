package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.Nodes;
import com.cqupt.medical.hypergraph.entity.Relationships;

import java.util.List;

public interface NodesService extends IService<Nodes> {
    List<Nodes> getAllNodes();

    List<Relationships> getRelationships();
}
