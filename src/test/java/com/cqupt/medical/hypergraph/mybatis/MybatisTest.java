package com.cqupt.medical.hypergraph.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.cqupt.medical.hypergraph.entity.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author EthanJ
 * @Date 2023/6/23 10:48
 * @Version 1.0
 */
@MybatisPlusTest
public class MybatisTest {

    @Autowired
    private TableMapper tableMapper;

    @Test
    void testSelectAll() {
        List<Table> tables = tableMapper.selectList(new QueryWrapper<Table>());
        System.out.println(tables);
    }
}
