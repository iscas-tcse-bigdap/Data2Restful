package com.iscas.k8scli.pojo.DTO;

import lombok.Data;

/**
 * @title createDeploymentInfo
 * @description  用于创建数据服务接口的参数
 * @author wbq
 * @version 1.0
 * @create 2023/10/11 10:44
 */
@Data
public class CreateDeploymentInfo {
    private String srcName;
    private String groupName;
}
