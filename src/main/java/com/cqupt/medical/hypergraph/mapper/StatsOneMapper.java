package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.StatsOne;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface StatsOneMapper extends BaseMapper<StatsOne> {
    Integer getDiseaseCount();

    Integer getSampleCount(String tableName);

    List<String> getTableNames();

    Date getEarlyDate(String tableName);

    Date getLastDate(String tableName);

    Integer getTaskCount();

    List<String> getUserBuildTableNames();
}
