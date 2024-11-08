package com.cqupt.medical.hypergraph;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cqupt.medical.hypergraph.mapper")
public class HypergraphApplication {

    public static void main(String[] args) {
        SpringApplication.run(HypergraphApplication.class, args);
    }
}
