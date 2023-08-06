package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.Table;
import com.cqupt.medical.hypergraph.mapper.TableMapper;
import com.cqupt.medical.hypergraph.service.TableService;
import com.cqupt.medical.hypergraph.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static com.cqupt.medical.hypergraph.util.Constants.*;

/**
 * @Author EthanJ
 * @Date 2023/6/23 19:07
 * @Version 1.0
 */
@Service
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements TableService {

    /**
     * 上传数据表
     *
     * @param tableFile
     * @return
     */
    @Override
    public String storeTable(MultipartFile tableFile) {
        String fileName = tableFile.getOriginalFilename();

        /*判断文件是否已经存在*/
        if (getOne(new QueryWrapper<Table>().eq("table_name", tableFile.getOriginalFilename())) != null)
            return new JsonUtil(FAIL_CODE, "存在同名文件，请重命名后重新上传", null).toJsonString();
//        String subName = fileName.substring(fileName.lastIndexOf("."));
//        String mainName = fileName.substring(0, fileName.lastIndexOf("."));
//        String uuid = UUID.randomUUID().toString();
        String filePath = TABLE_PATH + fileName;

        try {
            tableFile.transferTo(new File(filePath)); //上传文件到本地保存
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传出错了！", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("其他错误", e);
        }

        return new JsonUtil(SUCCESS_CODE, "数据表上传成功", null).toJsonString();
    }

    /**
     * 保存数据表信息
     *
     * @param tableInfo
     * @return
     */
    @Override
    public String saveTableInfo(Table tableInfo) {
        try {
            save(tableInfo);
            return new JsonUtil(SUCCESS_CODE, "保存数据表信息成功", null).toJsonString();
        } catch (TransactionException e) {
            e.printStackTrace();
            return new JsonUtil(FAIL_CODE, "保存数据表信息失败", null).toJsonString();
        }
    }

    /**
     * 获取当前所有数据表
     * 如果文件不存在，则删除对应数据库表项
     * 如果数据表大小未知，则读取文件大小对数据库进行更新
     * 返回更新后的列表
     *
     * @return
     */
    @Override
    public List<Table> getAllTables() {
        List<Table> tableList = list();
        Iterator<Table> tableIterator = tableList.iterator();
        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            String filePath = TABLE_PATH + table.getTableName();
            /*检查文件是否存在*/
            if (!(new File(filePath).exists())) {
                tableIterator.remove();
                remove(new QueryWrapper<Table>().eq("table_name", table.getTableName()));
                continue;
            }
            /*获取文件大小*/
            if (table.getTableSize() == null || table.getTableSize().equals("")) {
                File tableFile = new File(filePath);
                Long tableSize = tableFile.length();

                table.setTableSize(fileSizeConvert(tableSize));

                updateById(table);
            }
        }

        return tableList;
    }

    @Override
    public String deleteTable(String tableName) {
        /*删除文件*/
        String filePath = TABLE_PATH + tableName;
        File file = new File(filePath);
        if (file.delete()) {
            remove(new QueryWrapper<Table>().eq("table_name", tableName));  //删除数据库中表项
            return new JsonUtil(SUCCESS_CODE, "删除成功", null).toJsonString();
        } else
            return new JsonUtil(FAIL_CODE, "文件删除失败", null).toJsonString();
    }

    /**
     * 文件大小单位换算
     *
     * @param tableSize
     * @return
     */
    private static String fileSizeConvert(Long tableSize) {
        if (tableSize < 1024.0)
            return tableSize + "B";
        else if (tableSize >= 1024.0 && tableSize < 1024.0 * 1024.0)
            return String.format("%.2f", tableSize / 1024.0) + "KB";
        else
            return String.format("%.2f", tableSize / (1024.0 * 1024.0)) + "MB";
    }
}
