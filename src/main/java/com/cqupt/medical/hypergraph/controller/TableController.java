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
        return tableService.list();
    }

    @GetMapping("/table/{id}")
    public Table queryTableById(@PathVariable("id") Integer id) {
        return tableService.getById(id);
    }

    @GetMapping("tables/source/{source}")
    public List<Table> queryTablesBySource(@PathVariable("source") String source) {
        return tableService.list(new QueryWrapper<Table>().eq("source", source));
    }

    @GetMapping("tables/disease/{disease}")
    public List<Table> queryTablesByDisease(@PathVariable("disease") String disease) {
        return tableService.list(new QueryWrapper<Table>().eq("disease", disease));
    }

    @PostMapping("/table/upload")
    public String uploadTable(@RequestPart("data_table") MultipartFile tableFile, @RequestPart("table_info") Table table_info) {
        tableService.storeTable(tableFile, table_info);
        return "upload table success!";
    }

    @DeleteMapping("/table/delete/{id}")
    public String delTable(@PathVariable("id") Integer id){
        tableService.removeById(id);
        return "delete table success!";
    }
}
