package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.StatsOne;
import com.cqupt.medical.hypergraph.util.Result;

import java.util.List;


public interface StatsOneService extends IService<StatsOne> {
    StatsOne getStatsOne();

    Result getDiseases();

    List<String> getAllTableNames();
}
