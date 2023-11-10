package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.ColumnManager;
import com.cqupt.medical.hypergraph.entity.dto.ColumnManagerDTO;
import com.cqupt.medical.hypergraph.mapper.ColumnManagerMapper;
import com.cqupt.medical.hypergraph.service.ColumnManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author EthanJ
 * @Date 2023/10/15 23:04
 * @Version 1.0
 */
@Service
public class ColumnManagerServiceImpl extends ServiceImpl<ColumnManagerMapper, ColumnManager> implements ColumnManagerService {

//    @Autowired
//    private TableService tableService;

    @Autowired
    private ColumnManagerMapper columnManagerMapper;

//    @Override
//    public List<ColumnManager> getAllData() {
//        return list();
//    }

//    @Override
//    public List<String> getFieldByTableName(String tableName) {
//        return columnManagerMapper.getFieldByTableName(tableName);
//    }
//
//    @Override
//    public List<ColumnManager> getAllColumnManagerByTableName(List<String> tableNames) {
//        List<ColumnManager> res=new ArrayList<>();
//
//        for(int i=0;i<tableNames.size(); i++){
//            QueryWrapper queryWrapper=new QueryWrapper();
//
//            queryWrapper.eq("field_name",tableNames.get(i));
//            queryWrapper.eq("table_name","Diabetes");
//            ColumnManager columnManager = getOne(queryWrapper);
//            res.add(columnManager);
//        }
//        return res;
//    }

    @Override
    public void insertField(ColumnManagerDTO columnManagerDTO) {
        columnManagerMapper.insertColumn(columnManagerDTO);
    }

    @Override
    public void delFieldByTableName(String tableName){
        columnManagerMapper.delColumnByTableName(tableName);
    }
}
