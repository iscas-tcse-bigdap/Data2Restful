package com.iscas.apiservice.pojo.feignToK8scli;

import lombok.Data;

@Data
public class CreateHpaInfo {
    private String groupName;
    private int minValue;
    private int maxValue;

    public CreateHpaInfo(String name, int min, int max) {
        this.groupName = name;
        this.minValue = min;
        this.maxValue = max;
    }
}
