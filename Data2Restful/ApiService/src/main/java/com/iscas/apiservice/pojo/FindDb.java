package com.iscas.apiservice.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@title FindDb
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/9/13 15:45
 */
@Data
@NoArgsConstructor
public class FindDb {
    private String dbUrl;
    private String userName;
    private String passWord;
    private String driverClass;

    public FindDb(String url, String user, String pwd, String driver) {
        this.dbUrl = url;
        this.userName = user;
        this.passWord = pwd;
        this.driverClass = driver;
    }
}
