package com.cqupt.medical.hypergraph.vo;

import com.cqupt.medical.hypergraph.entity.FilterDataCol;
import com.cqupt.medical.hypergraph.entity.FilterDataInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterConditionVo {
    private FilterDataInfo filterDataInfo;
    private List<FilterDataCol> filterDataCols;
}
