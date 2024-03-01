package com.iscas.k8scli.pojo;

import lombok.Data;

/**
 * @ClassName: InstanceInfo
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/20 11:46
 */
@Data
public class InstanceInfo {
    private String name;
    private String createTime;
    private String innerIp;
    private String uid;
}
