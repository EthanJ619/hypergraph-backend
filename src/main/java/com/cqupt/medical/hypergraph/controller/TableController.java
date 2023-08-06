package com.cqupt.medical.hypergraph.controller;

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
    public Table queryTableById(@PathVariable("id") Integer id) {
        return tableService.getById(id);
    }

    @PostMapping("/table/add_info")
    public String addTableInfo(@RequestBody Table tableInfo){
        return tableService.saveTableInfo(tableInfo);
    }

    @PostMapping("/table/upload")
    public String uploadTable(@RequestPart("data_table") MultipartFile tableFile){
        return tableService.storeTable(tableFile);
    }

//    @PostMapping("/table/upload")
//    public String uploadTable(@RequestPart("data_table") MultipartFile tableFile, @RequestPart("table_info") String tableInfoStr) throws JsonProcessingException {
//        Table tableInfo = new ObjectMapper().readValue(tableInfoStr, Table.class);  //将收到的json字符串转化为对象
//        return tableService.storeTable(tableFile, tableInfo);
//    }

    @DeleteMapping("/table/delete/{table_name}")
    public String delTable(@PathVariable("table_name") String tableName) {
        return tableService.deleteTable(tableName);
    }
}
