package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.LogEntity;
import com.cqupt.medical.hypergraph.entity.User;
import com.cqupt.medical.hypergraph.mapper.LogMapper;
import com.cqupt.medical.hypergraph.mapper.UserMapper;
import com.cqupt.medical.hypergraph.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// TODO 公共模块新增类

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity>
        implements LogService {

    @Autowired
    LogMapper logMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<LogEntity> getAllLogs() {
        return logMapper.getAllLogs();
    }

    public void insertLog(String uid, Integer role, String operation) {
        User user = userMapper.selectByUid(uid);

        LogEntity logEntity = new LogEntity();
        logEntity.setUid(uid);
        logEntity.setUsername(user.getUsername());
        logEntity.setRole(role);
        logEntity.setOperation(operation);
        // 创建 DateTimeFormatter 对象，定义日期时间的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 创建 LocalDateTime 对象，存储当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 使用 formatter 格式化 LocalDateTime 对象
        String formattedDate = now.format(formatter);
        logEntity.setOpTime(formattedDate);

        logMapper.insert(logEntity);
    }
}
