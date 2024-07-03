package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.LogEntity;

import java.util.List;

// TODO 公共模块新增类
public interface LogService extends IService<LogEntity> {
    List<LogEntity> getAllLogs();
    void insertLog(String uid, Integer role, String operation);
}
