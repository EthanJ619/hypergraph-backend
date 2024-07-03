package com.cqupt.medical.hypergraph.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.medical.hypergraph.entity.Category2Entity;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2024/3/30 21:44
 * @Version 1.0
 */
public interface Category2Service extends IService<Category2Entity> {
    List<Category2Entity> getCategory();

    List<Category2Entity> getCategory2();

    void removeNode(String id);
}
