package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.Table;
import com.cqupt.medical.hypergraph.mapper.TableMapper;
import com.cqupt.medical.hypergraph.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author EthanJ
 * @Date 2023/6/23 19:07
 * @Version 1.0
 */
@Service
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements TableService {

    @Autowired
    private TableMapper tableMapper;

    @Override
    public void storeTable(MultipartFile tableFile, Table table) {
        String folderPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/table/";
        String fileName = tableFile.getOriginalFilename();
        String subName = fileName.substring(fileName.lastIndexOf("."));
        String mainName = fileName.substring(0, fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        fileName = mainName + uuid + subName;
        String filePath = folderPath + File.separator + '_' + fileName;
 
        try {
            tableFile.transferTo(new File(filePath));
            table.setTableName(fileName);
            save(table);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传出错了！", e);
        } catch (TransactionException e) {
            e.printStackTrace();
            throw new RuntimeException("添加数据表信息出错！", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("其他错误", e);
        }
    }
}
