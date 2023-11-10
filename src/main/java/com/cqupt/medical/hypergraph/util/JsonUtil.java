package com.cqupt.medical.hypergraph.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author EthanJ
 * @Date 2023/8/6 10:48
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonUtil<T> {

    private String code;
    private String message;
    private T data;

    private Integer pageCount;

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{'code':'500','message':'服务器内部错误：JsonProcessingException'}";
        }
    }

    public JsonUtil(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
