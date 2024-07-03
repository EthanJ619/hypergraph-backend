package com.cqupt.medical.hypergraph.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cqupt.medical.hypergraph.util.Result;
import com.cqupt.medical.hypergraph.entity.Category2Entity;
import com.cqupt.medical.hypergraph.entity.CategoryEntity;
import com.cqupt.medical.hypergraph.entity.TableDescribeEntity;
import com.cqupt.medical.hypergraph.entity.UserLog;
import com.cqupt.medical.hypergraph.mapper.CategoryMapper;
import com.cqupt.medical.hypergraph.mapper.TableDescribeMapper;
import com.cqupt.medical.hypergraph.mapper.UserMapper;
import com.cqupt.medical.hypergraph.service.Category2Service;
import com.cqupt.medical.hypergraph.service.CategoryService;
import com.cqupt.medical.hypergraph.service.UserLogService;
import com.cqupt.medical.hypergraph.vo.AddDiseaseVo;
import com.cqupt.medical.hypergraph.vo.DeleteDiseaseVo;
import com.cqupt.medical.hypergraph.vo.UpdateDiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

// TODO 公共模块新增类
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    Category2Service category2Service;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    TableDescribeMapper tableDescribeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserLogService userLogService;

    // 获取目录
    @GetMapping("/category")
    public Result<List<CategoryEntity>> getCatgory(@RequestParam String uid){
        List<CategoryEntity> list = categoryService.getCategory(uid);
//        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    @GetMapping("/Taskcategory")
    public Result<List<CategoryEntity>> getCatgory(){
        List<CategoryEntity> list = categoryService.getTaskCategory();
//        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    // 字段管理获取字段
    @GetMapping("/category2")
    public Result<List<Category2Entity>> getCatgory2(){
        List<Category2Entity> list = category2Service.getCategory2();
        return Result.success("200",list);
    }


    // 创建一种新的疾病
    @PostMapping("/addDisease")
    public Result addDisease(@RequestBody CategoryEntity categoryNode){
        System.out.println("参数为："+ JSON.toJSONString(categoryNode));
        categoryService.save(categoryNode);
        return Result.success(200,"新增目录成功");
    }

    // 删除一个目录
    @Transactional
    @GetMapping("/category/remove")
    public Result removeCate(CategoryEntity categoryEntity){
        System.out.println("要删除的目录为："+JSON.toJSONString(categoryEntity));
        if(categoryEntity.getIsLeafs()==0){
            categoryService.removeNode(categoryEntity.getId());
        }
        else {
            categoryService.removeNode(categoryEntity.getId(),categoryEntity.getLabel());
            TableDescribeEntity tableDescribeEntity = tableDescribeMapper.selectOne(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
            if(tableDescribeEntity.getTableSize()!=0){
                userMapper.recoveryUpdateUserColumnById(String.valueOf(Integer.parseInt(tableDescribeEntity.getUid())),new Double(tableDescribeEntity.getTableSize()));
            }
            tableDescribeMapper.delete(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
//            tTableMapper.delete(new QueryWrapper<tTable>().eq("table_name",categoryEntity.getLabel()));

        }
        return Result.success(200,"删除成功");
    }
    @GetMapping("/changeStatus")
    public Result changeStatus(CategoryEntity categoryEntity){
        categoryService.changeStatus(categoryEntity);
        return Result.success(200,"修改成功",null);
    }

//    @GetMapping("/addParentDisease")
//    public Result addParentDisease(@RequestParam("diseaseName") String diseaseName){
//        System.out.println("name:"+diseaseName);
//        categoryService.addParentDisease(diseaseName);
//        return Result.success(200,"添加成功");
//    }


    /**
     *
     * @return
     */
//    zongqing新增疾病管理模块
    @GetMapping("/category/getAllDisease")
    public Result<List<CategoryEntity>> getAllDisease(){
        List<CategoryEntity> list = categoryService.getAllDisease();
        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    /**
     * 修改。加了isNULL条件
     */
    @GetMapping("/category/checkDiseaseName/{diseaseName}")
    public Result checkDiseaseName(@PathVariable String diseaseName){
        QueryWrapper<CategoryEntity> queryWrapper = Wrappers.query();
        queryWrapper.eq("label", diseaseName)
                .eq("is_delete", 0)
                .isNull("status");
        CategoryEntity category = categoryMapper.selectOne(queryWrapper);
        return category==null?Result.success("200","病种名可用"):Result.fail("400","病种名已存在");
    }

    /**
     * 修改
     * @param
     * @return
     */
    @PostMapping("/category/addCategory")
    public Result addCategory(@RequestBody AddDiseaseVo addDiseaseVo){
        if(categoryService.addCategory(addDiseaseVo)>0){
            UserLog userLog = new UserLog(null,Integer.parseInt(addDiseaseVo.getUid()),new Date(),"添加病种"+addDiseaseVo.getFirstDisease(),addDiseaseVo.getUsername());
            userLogService.save(userLog);
            return Result.success("添加病种成功");
        }else{
            UserLog userLog = new UserLog(null,Integer.parseInt(addDiseaseVo.getUid()),new Date(),"添加病种失败",addDiseaseVo.getUsername());
            userLogService.save(userLog);
            return Result.fail("添加病种失败");
        }
    }
    @PostMapping("/category/updateCategory")
    public Result updateCategory(@RequestBody UpdateDiseaseVo updateDiseaseVo){
        UserLog userLog = new UserLog(null,Integer.parseInt(updateDiseaseVo.getUid()),new Date(),"修改病种"+updateDiseaseVo.getOldName()+"为"+updateDiseaseVo.getDiseaseName(),updateDiseaseVo.getUsername());
        userLogService.save(userLog);
        return categoryService.updateCategory(updateDiseaseVo);
    }
    @PostMapping("/category/deleteCategory")
    public Result deleteCategory(@RequestBody DeleteDiseaseVo deleteDiseaseVo){
        StringJoiner joiner = new StringJoiner(",");
        for (String str : deleteDiseaseVo.getDeleteNames()) {
            joiner.add(str);
        }
        UserLog userLog = new UserLog(null,Integer.parseInt(deleteDiseaseVo.getUid()),new Date(),"删除病种："+joiner.toString(),deleteDiseaseVo.getUsername());
        userLogService.save(userLog);
        categoryService.removeCategorys(deleteDiseaseVo.getDeleteIds());
        return Result.success("删除成功");
    }


}

