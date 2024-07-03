package com.cqupt.medical.hypergraph.controller;

import com.alibaba.fastjson.JSON;
import com.cqupt.medical.hypergraph.entity.FeatureEntity;
import com.cqupt.medical.hypergraph.service.FeatureManageService;
import com.cqupt.medical.hypergraph.util.FeatureType;
import com.cqupt.medical.hypergraph.util.Result;
import com.cqupt.medical.hypergraph.vo.FeatureListVo;
import com.cqupt.medical.hypergraph.vo.FeatureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2024/3/30 21:14
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    @Autowired
    FeatureManageService featureManageService;

    @GetMapping("/getFeatures")
    public Result<FeatureEntity> getFeature(@RequestParam("index") Integer belongType){ // belongType说明是属于诊断类型、检查类型、病理类型、生命特征类型
        String type = null;
        for (FeatureType value : FeatureType.values()) {
            if(value.getCode() == belongType){
                type = value.getName();
            }
        }
        List<FeatureVo> list = featureManageService.getFeatureList(type);
        return Result.success("200",list);
    }


    // TODO 废弃方法
    @PostMapping("/insertFeature") // 上传特征分类结果
    public Result fieldInsert(@RequestBody FeatureListVo featureListVo){
        System.out.println("tableHeaders:"+ JSON.toJSONString(featureListVo));

        featureManageService.insertFeatures(featureListVo);
        return null;
    }
}
