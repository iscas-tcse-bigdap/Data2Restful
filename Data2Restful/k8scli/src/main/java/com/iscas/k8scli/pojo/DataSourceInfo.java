package com.iscas.k8scli.pojo;

import lombok.Data;

/**
 *@title DataSourceInfo
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/9/21 13:23
 */

@Data
public class DataSourceInfo {
    private String srcType;
    private String dsName;
    private String host;
    private String port;
    private String user;
    private String pwd;
    private String db;
}
