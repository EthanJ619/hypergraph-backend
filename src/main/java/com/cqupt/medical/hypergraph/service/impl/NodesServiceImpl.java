package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.Nodes;
import com.cqupt.medical.hypergraph.entity.Relationships;
import com.cqupt.medical.hypergraph.mapper.NodesMapper;
import com.cqupt.medical.hypergraph.service.NodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodesServiceImpl extends ServiceImpl<NodesMapper, Nodes>
        implements NodesService {
    @Autowired
    NodesMapper nodesMapper;


    @Override
    public List<Nodes> getAllNodes() {
        return nodesMapper.getAllNodes();
    }

    @Override
    public List<Relationships> getRelationships() {
        return nodesMapper.getRelationships();
    }
}
