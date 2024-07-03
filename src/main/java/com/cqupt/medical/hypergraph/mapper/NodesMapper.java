package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.Nodes;
import com.cqupt.medical.hypergraph.entity.Relationships;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodesMapper extends BaseMapper<Nodes> {
    List<Nodes> getAllNodes();

    List<Relationships> getRelationships();
}
