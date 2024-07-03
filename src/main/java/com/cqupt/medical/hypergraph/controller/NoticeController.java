package com.cqupt.medical.hypergraph.controller;


import com.cqupt.medical.hypergraph.entity.Notification;
import com.cqupt.medical.hypergraph.service.NoticeService;
import com.cqupt.medical.hypergraph.util.Result;
import com.cqupt.medical.hypergraph.vo.InsertNoticeVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/allNotices")
    public PageInfo<Notification> allNotices(@RequestParam Integer pageNum , @RequestParam Integer pageSize){
        return noticeService.allNotices(pageNum, pageSize);
    }

    @GetMapping("/queryNotices")
    public List<Notification> queryNotices(){
        return noticeService.queryNotices();
    }




    @PostMapping("/updateNotice")
    public Result updateNotice(@RequestBody Notification notification){

        notification.setUpdateTime(new Date());
        noticeService.saveOrUpdate(notification);

        return new Result<>(null , "成功", 200);
    }


    @PostMapping("delNotice")
    public Result delNotice(@RequestBody Notification notification){
        noticeService.removeById(notification.getInfoId());
        return new Result<>(null , "成功", 200);
    }

    @PostMapping("insertNotice")
    public Result insertNotice(@RequestBody InsertNoticeVo notification){

        noticeService.saveNotification(notification);
        return new Result<>(null , "成功", 200);
    }


}
