package com.iscas.k8scli.pojo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: ServiceInfo
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/20 11:47
 */
@Data
public class ServiceInfo {
    private AppInfo appInfo;
    private String startTime;
    private String uID;
    private String status;
    private int instanceNum;
    private List<InstanceInfo> instances;
}
