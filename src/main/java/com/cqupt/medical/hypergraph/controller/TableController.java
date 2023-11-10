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

    @GetMapping("/tables/{pageNum}")
    public JsonUtil<List<Table>> queryTablesPagination(@PathVariable("pageNum") int pageNum) {
        PageHelper.startPage(pageNum, 15);
        List<Table> tables = tableService.getAllTables();
        PageInfo<Table> tablePageInfo = new PageInfo<>(tables);
        return new JsonUtil<>(SUCCESS_CODE, "获取数据表成功", tables, tablePageInfo.getPages());
    }

    @GetMapping("tables")
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
