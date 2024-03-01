package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.mapper.DataSourceMapper;
import com.iscas.apiservice.pojo.DataSource;
import com.iscas.apiservice.pojo.FindDb;
import com.iscas.apiservice.service.DatabaseService;
import com.iscas.apiservice.utils.Response;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wbq
 * @version 1.0
 * @title DatabaseServiceImpl
 * @description
 * @create 2023/9/13 15:11
 */

@Service
public final class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取当前数据库下的表.
     */
    @Override
    public List<String> getCurrentTableList() {
        String sql = "SHOW TABLES";
        List<String> tableList = jdbcTemplate.queryForList(sql, String.class);
        return tableList;
    }

    /**
     * 获取mysql数据库下的表.
     */
    @Override
    public List<Map<String, Object>> getTableList(FindDb findDb) throws SQLException {
        Connection conn = this.getConn(findDb.getDbUrl(), findDb.getUserName(), findDb.getPassWord(),
            findDb.getDriverClass());
        if (conn == null) {
            return null;
        }
        JdbcTemplate temporaryJdbcTemplate = new JdbcTemplate();
        temporaryJdbcTemplate.setDataSource(new SingleConnectionDataSource(conn, true));

        List<Map<String, Object>> tableList = new ArrayList<>();

        DatabaseMetaData metaData = conn.getMetaData();
        String catalog = conn.getCatalog(); // 获取当前连接的数据库名称
        ResultSet resultSet = metaData.getTables(catalog, null, null, new String[]{"TABLE"});
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");

            List<Map<String, String>> columnList = new ArrayList<>();

            // 获取表的列信息
            ResultSet columnsResultSet = metaData.getColumns(catalog, null, tableName, null);
            while (columnsResultSet.next()) {
                String columnName = columnsResultSet.getString("COLUMN_NAME");
                Map<String, String> columnMap = new HashMap<>();
                columnMap.put("label", columnName);
                columnList.add(columnMap);
            }
            columnsResultSet.close();

            Map<String, Object> tableMap = new HashMap<>();
            tableMap.put("label", tableName);
            tableMap.put("children", columnList);

            tableList.add(tableMap);
        }
        resultSet.close();
        return tableList;
    }


    @Override
    public List<Map<String, Object>> getMongoDictList(String host, String port, String database, String user,
                                                      String pwd) {
        MongoClient mongoClient = getMongoClient(host, port, database, user, pwd);
        MongoDatabase db = mongoClient.getDatabase(database);
        List<Map<String, Object>> dictList = new ArrayList<>();
        for (String collectionName : db.listCollectionNames()) {
            Map<String, Object> dictMap = new HashMap<>();
            dictMap.put("label", collectionName);
            dictMap.put("children", null);
            dictList.add(dictMap);
        }
        return dictList;
    }

    public Connection getConn(String url, String userName, String passWord, String driverClass) {
        //声明数据库连接对象
        Connection conn = null;
        try {
            //加载驱动
            Class.forName(driverClass);
            //初始化数据库连接,获取连接对象
            conn = DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            System.out.println("获得数据库连接出错:" + e.getMessage());
        }
        return conn;
    }

    @Override
    public ArrayList<DataSource> getDataSourceList() {
        return dataSourceMapper.getDataSourceList();
    }

    @Override
    public DataSource getDataSourceByID(int srcId) {
        return dataSourceMapper.getDataSourceByID(srcId);
    }

    @Override
    public int createDataSource(DataSource dataSource) {
        try {
            dataSourceMapper.createDataSource(dataSource);
            return dataSource.getSrcId();
        } catch (Exception e) {
            // 主要捕获唯一性约束的错误，以及一些未知错误
            return -1;
        }
    }

    @Override
    public int deleteDataSource(int srcId) {
        return dataSourceMapper.deleteDataSource(srcId);
    }

    @Override
    public Response testConnectionMysql(String url, String driverClass, String user, String pwd) throws SQLException {
        Connection connection = getConn(url, user, pwd, driverClass);
        if (connection != null) {
            connection.close();
            return Response.ok().message("数据源连接测试成功！");
        }
        return Response.error().message("数据源连接失败");
    }

    @Override
    public Response testConnectionMongoDB(String host, String port, String database, String user, String pwd) {
        MongoClient mongoClient = getMongoClient(host, port, database, user, pwd);
        if (mongoClient == null) {
            return Response.error().message("数据源连接失败");
        }
        mongoClient.listDatabases();
        mongoClient.getDatabase(database);
        mongoClient.close();
        return Response.ok().message("数据源连接测试成功");
    }

    public MongoClient getMongoClient(String host, String port, String database, String user, String pwd) {
        MongoClient mongoClient = new MongoClient(host, Integer.parseInt(port));
        // 这一部分是 对需要密码的mongo进行连接测测试， 暂时没有合适的mongo数据库进行测试
//        try {
//            mongoClient.listDatabases();
//        } catch (MongoException e) {
//            String connectionString = "mongodb://" + user + ":" + pwd + "@" + host + ":" + port + "/" + database;
//            MongoClientURI uri = new MongoClientURI(connectionString);
//            mongoClient = new MongoClient(uri);
//        }
        try {
            ListDatabasesIterable<Document> documents = mongoClient.listDatabases();
            for (Document document : documents) {
                System.out.println(document.toJson());
            }
        } catch (MongoException e) {
            return null;
        }
        return mongoClient;
    }

    @Override
    public int updateDataSource(DataSource dataSource) {
        return dataSourceMapper.updateDataSource(dataSource);
    }
}
