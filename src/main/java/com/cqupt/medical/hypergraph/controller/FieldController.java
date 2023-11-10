package com.cqupt.medical.hypergraph.controller;

import com.cqupt.medical.hypergraph.entity.ColumnManager;
import com.cqupt.medical.hypergraph.entity.Table;
import com.cqupt.medical.hypergraph.entity.dto.ColumnManagerDTO;
import com.cqupt.medical.hypergraph.service.ColumnManagerService;
import com.cqupt.medical.hypergraph.service.TableService;
import com.cqupt.medical.hypergraph.util.JsonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cqupt.medical.hypergraph.util.Constants.FAIL_CODE;
import static com.cqupt.medical.hypergraph.util.Constants.SUCCESS_CODE;

/**
 * @Author EthanJ
 * @Date 2023/11/4 17:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/fieldManager")
public class FieldController {

    @Autowired
    private ColumnManagerService columnManagerService;

    @Autowired
    private TableService tableService;

    /**
     * 上传数据表的字段信息
     *
     * @param columnManagerDTO
     * @return
     */
    @PostMapping("/addField")
    public JsonUtil<List<Table>> addField(@RequestBody ColumnManagerDTO columnManagerDTO) {
        try {
            columnManagerService.insertField(columnManagerDTO);
            return new JsonUtil<>(SUCCESS_CODE, "添加字段成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonUtil<>(FAIL_CODE, "添加字段失败", null);
        }
    }

    @PostMapping("/updateField")
    public JsonUtil updateField(@RequestBody ColumnManager columnManager) {
        columnManagerService.updateById(columnManager);
        return new JsonUtil<>(SUCCESS_CODE, "修改字段成功", null);
    }

    @PostMapping("/delField")
    public JsonUtil delField(@RequestBody ColumnManager columnManager) {
        boolean res = columnManagerService.removeById(columnManager.getId());
        if (res)
            return new JsonUtil<>(SUCCESS_CODE, "删除字段成功", null);
        else return new JsonUtil<>(FAIL_CODE, "删除字段失败", null);
    }

    /**
     * 查询所有字段
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/fields")
    public JsonUtil<List<ColumnManager>> queryAllFields(@RequestParam("pageNum") int pageNum) {
        PageHelper.startPage(pageNum, 15);
        List<ColumnManager> allColumns = columnManagerService.list();
        PageInfo pageInfo = new PageInfo<>(allColumns);

        return new JsonUtil<>(SUCCESS_CODE, "查询字段成功", allColumns, pageInfo.getPages());
    }
}
