package com.cqupt.medical.hypergraph.controller;

import com.cqupt.medical.hypergraph.entity.FieldEntity;
import com.cqupt.medical.hypergraph.service.CategoryService;
import com.cqupt.medical.hypergraph.service.FieldManageService;
import com.cqupt.medical.hypergraph.service.TableDataService;
import com.cqupt.medical.hypergraph.service.TableDescribeService;
import com.cqupt.medical.hypergraph.util.Result;
import com.cqupt.medical.hypergraph.vo.QueryFieldVO;
import com.cqupt.medical.hypergraph.vo.UpdateFieldVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/11/4 17:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/field")
public class FieldController {

    @Autowired
    TableDataService tableService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    TableDescribeService tableDescService;

    @Autowired
    private FieldManageService fieldManageService;


    /**
     * 通过关联疾病名称展示字段信息
     *
     * @param
     * @return 字段信息表
     */
    @PostMapping("/getAllField")
    public Result getAllField(@RequestBody QueryFieldVO queryFieldVO) {
        System.out.println(queryFieldVO.getDiseaseName());
        List<FieldEntity> res = fieldManageService.getFieldByDiseaseName(queryFieldVO.getDiseaseName());
        return Result.success(res);
    }


    /**
     * 新建特征表 根据动态选择来更新字段表
     *
     * 接收病种名字 和 字段列表
     */
    @PostMapping("/updateField")
    public Result updateField(@RequestBody UpdateFieldVO updateFieldVO) {
        String diseaseName = updateFieldVO.getDiseaseName();
        List<String> fields = updateFieldVO.getFields();
        // 更新字段表信息
        fieldManageService.updateFieldsByDiseaseName(diseaseName, fields);
        return Result.success(null);
    }

}
