package com.cqupt.medical.hypergraph.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.medical.hypergraph.entity.Table;
import com.cqupt.medical.hypergraph.service.TableService;
import com.cqupt.medical.hypergraph.util.JsonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.cqupt.medical.hypergraph.util.Constants.SUCCESS_CODE;

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
    public JsonUtil<List<Table>> queryAllTables(int pageNum) {
        List<Table> tables = new ArrayList<>();
        if (pageNum == 1)
            tables = tableService.getAllTables();

        PageHelper.startPage(pageNum, 15);
        List<Table> tablesPagnation = tableService.getAllTables();
        PageInfo<Table> tablePageInfo = new PageInfo<>(tablesPagnation);
        return new JsonUtil<>(SUCCESS_CODE, "获取数据表成功", tables, tablesPagnation, tablePageInfo.getPages());
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
