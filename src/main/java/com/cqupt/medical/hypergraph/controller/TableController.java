package com.cqupt.medical.hypergraph.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.medical.hypergraph.entity.Table;
import com.cqupt.medical.hypergraph.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/15 9:07
 * @Version 1.0
 */
@RestController
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping("/tables")
    public List<Table> queryAllTables() {
        return tableService.getAllTables();
    }

    @GetMapping("/table/{table_name}")
    public Table queryTableByName(@PathVariable("table_name") String tableName) {
        return tableService.getOne(new QueryWrapper<Table>().eq("table_name", tableName));
    }

    @PostMapping("/table/add_info")
    public String addTableInfo(@RequestBody Table tableInfo) {
        return tableService.saveTableInfo(tableInfo);
    }

    @PostMapping("/table/upload")
    public String uploadTable(@RequestPart("data_table") MultipartFile tableFile) {
        return tableService.storeTable(tableFile);
    }

//    @PostMapping("/table/upload")
//    public String uploadTable(@RequestPart("data_table") MultipartFile tableFile, @RequestPart("ta ble_info") String tableInfoStr) throws JsonProcessingException {
//        Table tableInfo = new ObjectMapper().readValue(tableInfoStr, Table.class);  //将收到的json字符串转化为对象
//        return tableService.storeTable(tableFile, tableInfo);
//    }

    @DeleteMapping("/table/delete/{table_name}")
    public String delTable(@PathVariable("table_name") String tableName) {
        return tableService.deleteTable(tableName);
    }

    @PostMapping("/table/features/{table_name}")
    public String getTableFeatures(@PathVariable("table_name") String tableName) {
        return tableService.queryTableFeatures(tableName);
    }
}
