package com.iscas.apiservice.service;

import com.iscas.apiservice.pojo.DataSource;
import com.iscas.apiservice.pojo.FindDb;
import com.iscas.apiservice.utils.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *@title DatabaseService
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/9/13 15:09
 */

public interface DatabaseService {
    List<String> getCurrentTableList();

    List<Map<String, Object>> getTableList(FindDb findDb) throws SQLException;

    List<Map<String, Object>> getMongoDictList(String host, String port, String database, String user, String pwd);

    ArrayList<DataSource> getDataSourceList();

    DataSource getDataSourceByID(int srcId);

    int createDataSource(DataSource dataSource);

    int deleteDataSource(int srcId);

    Response testConnectionMysql(String url, String driverClass, String user, String pwd) throws SQLException;

    Response testConnectionMongoDB(String host, String port, String database, String user, String pwd);

    int updateDataSource(DataSource dataSource);
}
