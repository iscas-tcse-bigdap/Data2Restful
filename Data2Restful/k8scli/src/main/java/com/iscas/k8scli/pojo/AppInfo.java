package com.iscas.k8scli.pojo;

import lombok.Data;

/**
 * @ClassName: AppInfo
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/20 11:46
 */

@Data
public class AppInfo {
    private String name;            // metadata-name, selector-app, labels-app, containers-name
    private String image;           // template-spec-image
    private String code;
    private int containerPort;      //
    private int initInstanceNum;    // spec-replicas
    private int servicePort;        // service-spec-ports-nodePort
}
