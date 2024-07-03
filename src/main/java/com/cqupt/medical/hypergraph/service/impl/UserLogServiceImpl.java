package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.UserLog;
import com.cqupt.medical.hypergraph.mapper.UserLogMapper;
import com.cqupt.medical.hypergraph.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【user_log】的数据库操作Service实现
* @createDate 2023-09-07 14:34:13
*/
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog>
    implements UserLogService {

    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public int insertUserLog(UserLog userLog) {

        int insert = userLogMapper.insert(userLog);
        return insert;
    }
}




