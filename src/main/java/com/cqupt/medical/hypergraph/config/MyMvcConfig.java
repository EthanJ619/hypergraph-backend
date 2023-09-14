package com.cqupt.medical.hypergraph.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author EthanJ
 * @Date 2023/9/14 21:45
 * @Version 1.0
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加映射路径
        registry.addMapping("/**")   // 添加映射路径
                .allowedOriginPatterns("*")  //允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true) // 允许发送凭证信息（如cookies）
                .maxAge(3600); // 预检请求的有效期，单位秒
    }
}
