package com.iscas.k8scli.pojo.DTO;

import lombok.Data;

/**
 * @author wbq
 * @version 1.0
 * @title CreateHpaInfo
 * @description
 * @create 2023/11/16 9:26
 */

@Data
public class CreateHpaInfo {
    private String groupName;
    private int minValue;
    private int maxValue;
}
