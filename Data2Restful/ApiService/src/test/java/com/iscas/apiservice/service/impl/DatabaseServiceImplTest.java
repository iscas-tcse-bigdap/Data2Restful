package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.pojo.DataSource;
import com.iscas.apiservice.pojo.FindDb;
import com.iscas.apiservice.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author wbq
 * @version 1.0
 * @title DatabaseServiceImplTest
 * @description
 * @create 2023/12/13 13:34
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("DatabaseServiceImplTest test")
@RunWith(MockitoJUnitRunner.class)
@Rollback
@Slf4j
class DatabaseServiceImplTest {
    private static final List<String> TABLE_LIST = new ArrayList<>(Arrays.asList("api", "apigroup", "datasource",
        "menu",
        "params", "plugin_acl", "plugin_acl_detail", "plugin_dynamic_expansion", "plugin_dynamic_rate_limit",
        "plugin_key_auth", "role", "s_role_menu", "s_user_role", "user"));
    private static final FindDb FIND_DB = new FindDb("jdbc:mysql://60.245.209.102:13306/test", "root", "Oncedi@2020",
        "com.mysql.cj.jdbc.Driver");
    private static DataSource DATA_SOURCE1 = new DataSource(1, "datacenter", "mysql", "com.mysql.cj.jdbc.Driver", "60" +
        ".245.209.102", "13306", "datacenter", "root", "Oncedi@2020", "数据中台", 1);
    private static DataSource DATA_SOURCE2 = new DataSource(18, "k8scli", "mysql", "com.mysql.cj.jdbc.Driver", "60" +
        ".245.209.102", "13306", "test", "root", "Oncedi@2020", "", 1);
    private static DataSource DATA_SOURCE3 = new DataSource(20, "mongo-user", "mongodb", "", "60.245.209.163", "27017"
        , "user", "root", "123", "mongo测试数据库-user", 1);
    private static DataSource DATA_SOURCE_WITHOUT_ID = new DataSource(0, "datacenter2", "mysql", "com.mysql.cj.jdbc" +
        ".Driver", "60.245.209.102", "13306", "datacenter", "root", "Oncedi@2020", "数据中台", 1);
    private static final ArrayList<DataSource> DATA_SOURCE_LIST = new ArrayList<>(Arrays.asList(DATA_SOURCE1,
        DATA_SOURCE2, DATA_SOURCE3));

    @Autowired
    @InjectMocks
    DatabaseServiceImpl databaseServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentTableList() {
        List<String> result = databaseServiceImpl.getCurrentTableList();
        Assertions.assertEquals(TABLE_LIST, result);
    }

    @Test
    void testGetTableList() throws SQLException {
//        List<Map<String, Object>> result = databaseServiceImpl.getTableList(FIND_DB);
//        Assertions.assertEquals(Arrays.asList(new HashMap<String, Object>() {{
//            put("label", "appinfo");
//            put("children", Arrays.<Map<String, Object>>asList(
//                new HashMap<String, Object>() {{put("label", "name");}},
//                new HashMap<String, Object>() {{put("label", "image");}},
//                new HashMap<String, Object>() {{put("label", "initInstanceNum");}},
//                new HashMap<String, Object>() {{put("label", "servicePort");}},
//                new HashMap<String, Object>() {{put("label", "containerPort");}},
//                new HashMap<String, Object>() {{put("label", "code");}}));
//        }}), result);
        // correct situation
        Assertions.assertNotNull(databaseServiceImpl.getTableList(FIND_DB));
        // error situation with uncorrected port.
        Assertions.assertNull(databaseServiceImpl.getTableList(new FindDb("jdbc:mysql://60.245.209.102:106/test",
            "root", "Oncedi@2020", "com.mysql.cj.jdbc.Driver")));
    }

    @Test
    void testGetMongoDictList() {
        List<Map<String, Object>> result = databaseServiceImpl.getMongoDictList("60.245.209.163", "27017", "user",
            "root", "123");
        Assertions.assertEquals(Arrays.<Map<String, Object>>asList(new HashMap<String, Object>() {{
            put("label", "class");
            put("children", null);
        }}, new HashMap<String, Object>() {{
            put("label", "user_type");
            put("children", null);
        }}), result);
    }

    @Test
    void testGetConn() {
        Connection result = databaseServiceImpl.getConn(FIND_DB.getDbUrl(), FIND_DB.getUserName(),
            FIND_DB.getPassWord(), FIND_DB.getDriverClass());
        Assertions.assertNotEquals(null, result);
    }

    @Test
    void testGetDataSourceList() {
        ArrayList<DataSource> result = databaseServiceImpl.getDataSourceList();
        Assertions.assertEquals(DATA_SOURCE_LIST, result);
    }

    @Test
    void testGetDataSourceByID() {
        DataSource result = databaseServiceImpl.getDataSourceByID(1);
        Assertions.assertEquals(DATA_SOURCE1, result);
    }

    @Test
    @Transactional
    void testCreateDataSource() {
        Assertions.assertTrue(databaseServiceImpl.createDataSource(DATA_SOURCE_WITHOUT_ID) > 0);
        Assertions.assertEquals(-1, databaseServiceImpl.createDataSource(DATA_SOURCE1));
    }

    @Test
    @Transactional
    void testDeleteDataSource() {
        Assertions.assertTrue(databaseServiceImpl.deleteDataSource(18) > 0);
        Assertions.assertEquals(0, databaseServiceImpl.deleteDataSource(0));
    }

    @Test
    void testTestConnectionMysql() throws SQLException {
        // correct situation
        Assertions.assertEquals(Response.ok().message("数据源连接测试成功！").getMessage(),
            databaseServiceImpl.testConnectionMysql(
            "jdbc:mysql://60.245.209.102:13306/test", "com.mysql.cj.jdbc.Driver", "root", "Oncedi@2020").getMessage());
        // uncorrected url
        Assertions.assertEquals(Response.error().message("数据源连接失败").getMessage(),
            databaseServiceImpl.testConnectionMysql(
            "jdbc:mysql://60.245.209.102/test", "com.mysql.cj.jdbc.Driver", "root", "Oncedi@2020").getMessage());
        // uncorrected auth
        Assertions.assertEquals(Response.error().message("数据源连接失败").getMessage(),
            databaseServiceImpl.testConnectionMysql(
            "jdbc:mysql://60.245.209.102:13306/test", "com.mysql.cj.jdbc.Driver", "root", "123").getMessage());
    }

    @Test
    void testTestConnectionMongoDB() {
        // mongodb 的连接测试只使用了port和host，而不针对database，
        // 且在目前的连接测试中，需要30s的自动断开连接后，才能说明mongodb连接失败

        // correct situation
        Assertions.assertEquals(Response.ok().message("数据源连接测试成功").getMessage(),
            databaseServiceImpl.testConnectionMongoDB(
            "60.245.209.163", "27017", "user", "root", "123").getMessage());
        // error situation
        Assertions.assertEquals(Response.error().message("数据源连接失败").getMessage(),
            databaseServiceImpl.testConnectionMongoDB(
            "60.245.209.1", "270", "user", "root", "123").getMessage());
    }

    @Test
    void testGetMongoClient() {
        // todo: 优化测试时间
        Assertions.assertNotNull(databaseServiceImpl.getMongoClient("60.245.209.163", "27017", "user", "root", "123"));
        Assertions.assertNull(databaseServiceImpl.getMongoClient("60.245.209.", "27017", "user", "root", "123"));
    }

    @Test
    @Transactional
    void testUpdateDataSource() {
        Assertions.assertTrue(databaseServiceImpl.updateDataSource(DATA_SOURCE2) > 0);
        Assertions.assertEquals(0, databaseServiceImpl.updateDataSource(new DataSource()));
    }
}