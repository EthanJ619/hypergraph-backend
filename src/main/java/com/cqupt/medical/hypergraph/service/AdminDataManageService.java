package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.AdminDataManage;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

// TODO 公共模块新增类
public interface AdminDataManageService extends IService<AdminDataManage> {
    List<String>  uploadDataTable(MultipartFile file, String pid, String tableName, String userName, String classPath, String uid, String tableStatus, float tableSize, String current_uid) throws IOException, ParseException, CsvException;

    List<AdminDataManage> selectAllDataInfo();

    List<AdminDataManage> selectDataByName(String searchType, String name);
    AdminDataManage selectDataById(String id);

    void deleteByTableName(String tablename);
    void deleteByTableId(String tableId);

    void updateById(String id,String[] pids,  String tableName, String tableStatus);
    void updateDataBaseTableName(String old_name, String new_name);

    void updateInfo(String id, String tableid, String oldTableName, String tableName, String tableStatus,String[] pids, String current_uid);
}
