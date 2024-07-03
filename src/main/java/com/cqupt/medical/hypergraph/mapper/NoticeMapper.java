package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.medical.hypergraph.entity.Notification;
import com.cqupt.medical.hypergraph.vo.InsertNoticeVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hp
* @description 针对表【Notification】的数据库操作Mapper
* @createDate 2023-05-16 16:44:39
* @Entity com.cqupt.software_1.entity.User
*/


@Mapper
public interface NoticeMapper extends BaseMapper<Notification> {

    Page<Notification> selectAllNotices();

    void saveNotification(InsertNoticeVo notification);
}




