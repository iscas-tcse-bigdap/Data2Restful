package com.iscas.apiservice.pojo.feignToK8scli;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@title createDeploymentInfo
 *@description  用于创建数据服务接口的参数
 *@author wbq
 *@version 1.0
 *@create 2023/10/11 10:44
 */
@Data
@NoArgsConstructor
public class CreateDeploymentInfo {
    private String srcName;
    private String groupName;

    public CreateDeploymentInfo(String newSrcName, String newGroupName) {
        this.srcName = newSrcName;
        this.groupName = newGroupName;
    }
}
