package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface LogMapper extends BaseMapper<LogEntity> {
    List<LogEntity> getAllLogs();
}
