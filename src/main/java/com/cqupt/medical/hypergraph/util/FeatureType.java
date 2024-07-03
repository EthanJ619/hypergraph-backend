package com.cqupt.medical.hypergraph.util;

// TODO 公共模块新增
public enum FeatureType {
    POPULATION(0, "population"),
    EXAMINE(1, "examine"),
    PHYSIOLOGY(2, "physiology"),
    SOCIETY(3, "society");

    private final int code;
    private final String name;

    FeatureType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
