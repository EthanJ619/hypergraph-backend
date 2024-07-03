package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.UserLog;


/**
* @author hp
* @description 针对表【user_log】的数据库操作Service
* @createDate 2023-09-07 14:34:13
*/
public interface UserLogService extends IService<UserLog> {

    int insertUserLog(UserLog userLog);
}
