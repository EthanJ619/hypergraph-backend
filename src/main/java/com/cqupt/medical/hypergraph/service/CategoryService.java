package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.util.Result;
import com.cqupt.medical.hypergraph.entity.CategoryEntity;
import com.cqupt.medical.hypergraph.vo.AddDiseaseVo;
import com.cqupt.medical.hypergraph.vo.UpdateDiseaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// TODO 公共模块新增类
public interface CategoryService extends IService<CategoryEntity> {
    List<CategoryEntity> getCategory(String uid);
    void removeNode(String id, String label);
    void removeNode(String id);
//    public void addParentDisease(String diseaseName);
//    void addParentDisease(String diseaseName);
    void changeStatus(CategoryEntity categoryEntity);

    List<CategoryEntity> getTaskCategory();
    List<CategoryEntity> getSpDisease();
    List<CategoryEntity> getComDisease();
    String getLabelByPid(@Param("pid") String pid);


    /**
     * 疾病管理模块
     * @return
     */
//    新增疾病管理模块
    List<CategoryEntity> getAllDisease();
    int addCategory(AddDiseaseVo addDiseaseVo);
    Result updateCategory(UpdateDiseaseVo updateDiseaseVo);
    void removeCategorys(List<String> deleteIds);


    //    下面方法是管理员端-数据管理新增
//    查看各等级病种
    List<CategoryEntity> getLevel2Label();
    List<CategoryEntity> getLabelsByPid(@Param("pid") String pid);

    String getLabelByNid(@Param("pid") String nid);

    void updateTableNameByTableName(String oldTableName, String tableName, String tableStatus);
}
