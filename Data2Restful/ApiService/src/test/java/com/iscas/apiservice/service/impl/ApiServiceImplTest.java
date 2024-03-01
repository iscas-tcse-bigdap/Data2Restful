package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.feign.K8scliFeign;
import com.iscas.apiservice.pojo.Api;
import com.iscas.apiservice.pojo.Parameter;
import com.iscas.apiservice.pojo.feignToK8scli.CreateDeploymentInfo;
import com.iscas.apiservice.pojo.webToController.ApiAndParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author wbq
 * @version 1.0
 * @title ApiServiceImplTest
 * @description
 * @create 2023/11/27 14:53
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("ApiServiceImpl test")
@RunWith(MockitoJUnitRunner.class)
@Rollback
@Slf4j
class ApiServiceImplTest {

    private static final ApiAndParam API_AND_PARAM_MOCK = new ApiAndParam(1, 1, 1, "junit-test", "junit-test", "junit" +
        "-test-code", "junit-test", 1);
    private static final ApiAndParam API_AND_PARAM_MOCK_PUBLISHED = new ApiAndParam(2, 1, 1, "junit-test", "junit" +
        "-test", "junit-test-code", "junit-test", 1);
    private static final ApiAndParam API_AND_PARAM_MOCK_WITHOUT_ID = new ApiAndParam(0, 1, 1, "junit-test", "junit" +
        "-test", "junit-test-code", "junit-test", 1);
    private static final Api API_MOCK = new Api(API_AND_PARAM_MOCK, new Date());
    private static final Api API_MOCK_PUBLISHED = new Api(API_AND_PARAM_MOCK_PUBLISHED, new Date());
    private static final Api API_MOCK_WITHOUT_ID = new Api(API_AND_PARAM_MOCK_WITHOUT_ID, new Date());
    private static final Parameter PARAMETER_MOCK = new Parameter("junit-test", "String", "junit-test-default-value",
        "junit-test-title");
    private static final Parameter PARAMETER_MOCK2 = new Parameter("junit-test2", "String", "junit-test-default" +
        "-value2", "junit-test-title2");

    @InjectMocks
    @Autowired
    ApiServiceImpl apiService;
    @Mock
    K8scliFeign k8scliFeign;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(k8scliFeign.deleteDeployment(any())).thenReturn("success");
        when(k8scliFeign.createDeployment(any(CreateDeploymentInfo.class))).thenReturn("success");
    }

    @Before
    public void before() {
        apiService.setK8scliFeign(k8scliFeign);
    }

    @BeforeAll
    static void beforeAll() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Transactional
    void createApi() {
        // correct situation
        Assertions.assertTrue(apiService.createApi(API_MOCK_WITHOUT_ID) > 0);
        // wrong situation of SQLIntegrityConstraintViolationException
        Assertions.assertFalse(apiService.createApi(API_MOCK_WITHOUT_ID) > 0);
        // wrong situation of without group_id, which is a required field
        Api api = API_MOCK_WITHOUT_ID;
        api.setApiId(api.getApiId() + 10086);
        api.setName(api.getName() + "-unique");
        api.setGroupId(-1);
        Assertions.assertFalse(apiService.createApi(api) > 0);
    }

    @Test
    void getApiList() {
        Assertions.assertNotNull(apiService.getApiList());
    }

    @Test
    void getApiInfoByApiId() {
        // correct situation
        Assertions.assertNotNull(apiService.getApiInfoByApiId(2));
        // api not exist
        Assertions.assertNull(apiService.getApiInfoByApiId(4));
    }

    @Test
    void getApiListByGroupId() {
        // correct situation
        Assertions.assertNotNull(apiService.getApiListByGroupId(1).getData());
        // group not exist
        Assertions.assertTrue(apiService.getApiListByGroupId(2).getData().isEmpty());
    }

    @Test
    void getCodeByApiId() {
        Assertions.assertNotNull(apiService.getCodeByApiId(1));
        Assertions.assertNull(apiService.getCodeByApiId(4));
    }

    @Test
    @Transactional
    void updateApi() {
        Assertions.assertTrue(apiService.updateApi(API_MOCK) > 0); // correct situation
    }

    @Test
    @Transactional
    void deleteApi() {
        Assertions.assertTrue(apiService.deleteApi(1) > 0);
    }

    @Test
    @Transactional
    void createApiAndAddParams() {
        // correct situation with no params
        int apiIdByNoParams = apiService.createApiAndAddParams(API_MOCK_WITHOUT_ID, new ArrayList<>());
        Assertions.assertTrue(apiIdByNoParams > 0);
        // wrong situation of SQLIntegrityConstraintViolationException
        int wrongSituation = apiService.createApiAndAddParams(API_MOCK, new ArrayList<>());
        Assertions.assertFalse(wrongSituation > 0);

        // correct situation with params
        List<Parameter> params = new ArrayList<>();
        params.add(PARAMETER_MOCK);
        params.add(PARAMETER_MOCK2);
        Api api = API_MOCK_WITHOUT_ID;
        api.setApiId(apiIdByNoParams + 233);
        api.setName("junit-test2");
        Assertions.assertTrue(apiService.createApiAndAddParams(api, params) > 0);
        // wrong situation with params cause by the same param_name
        List<Parameter> params2 = new ArrayList<>();
        params2.add(PARAMETER_MOCK);
        Parameter param_mock2 = PARAMETER_MOCK;
        param_mock2.setParamId(param_mock2.getParamId() + 10086);
        params2.add(param_mock2);
        api.setApiId(apiIdByNoParams + 233 * 2);
        api.setName("junit-test3");
        Assertions.assertFalse(apiService.createApiAndAddParams(api, params2) > 0);
    }

    @Test
    @Transactional
    void updateApiAndParams() {
        // 这一块主要测试api异常，主要有 1.api不存在 2.api_name重复 3.分组不存在
        List<Parameter> params = new ArrayList<>();
        params.add(PARAMETER_MOCK);
        // 1. api不存在
        Assertions.assertEquals(-1, apiService.updateApiAndParams(API_MOCK_WITHOUT_ID, params));
        // 2. api_name重复
        Api api_with_same_name = API_MOCK;
        api_with_same_name.setName("insert-test");
        Assertions.assertEquals(-1, apiService.updateApiAndParams(api_with_same_name, params));
        // 3. 分组不存在
        Api api_without_group = API_MOCK;
        api_without_group.setGroupId(2);
        Assertions.assertEquals(-1, apiService.updateApiAndParams(api_without_group, params));
        api_without_group.setGroupId(1);
        // 正确的情况
        Api api = API_MOCK;
        api.setName("junit-test-new");
        Assertions.assertEquals(1, apiService.updateApiAndParams(api, params));
    }

    @Test
    @Transactional
    void deleteApiAndParams() {
        Assertions.assertTrue(apiService.deleteApiAndParams(1) > 0);
        Assertions.assertFalse(apiService.deleteApiAndParams(4) > 0);
    }

    @Test
    @Transactional
    void publishApi() {
        // 对于已发布的api，重新发布会重启容器，不应报错
        Assertions.assertEquals(1, apiService.publishApi(API_MOCK));
        Assertions.assertEquals(1, apiService.publishApi(API_MOCK_PUBLISHED));
    }

    @Test
    @Transactional
    void terminateApi() {
        when(k8scliFeign.createDeployment(any(CreateDeploymentInfo.class))).thenReturn("success");
        when(k8scliFeign.deleteDeployment(any())).thenReturn("success");
        // 这里发布一个分组的两个api：
        // 要求终止第一个api返回1 代表直接终止，终止最后一个返回9 代表不再重启deployment
        Assertions.assertEquals(1, apiService.publishApi(API_MOCK));
        Assertions.assertEquals(1, apiService.publishApi(API_MOCK_PUBLISHED));
//        System.out.println(apiService.publishApi(API_MOCK));
//        System.out.println(apiService.publishApi(API_MOCK_PUBLISHED));
//        System.out.println(apiService.getApiInfoByApiId(API_MOCK.getApiId()).getStatus());
//        System.out.println(apiService.getApiInfoByApiId(API_MOCK_PUBLISHED.getApiId()).getStatus());
        Assertions.assertEquals(1, apiService.terminateApi(API_MOCK));
        Assertions.assertEquals(9, apiService.terminateApi(API_MOCK_PUBLISHED));

        // 测试k8s异常，删除deployment发生错误
        when(k8scliFeign.deleteDeployment(any(String.class))).thenThrow(new RuntimeException("junit-test"));
        Assertions.assertEquals(-3, apiService.terminateApi(API_MOCK));
    }

    @Test
    @Transactional
    void restartDeployment() {
        // 重启成功返回1
        // 集群出问题返回正确的错误提示
        when(k8scliFeign.createDeployment(any(CreateDeploymentInfo.class))).thenReturn("success");
        Assertions.assertEquals(1, apiService.restartDeployment(API_MOCK));
        when(k8scliFeign.createDeployment(any(CreateDeploymentInfo.class)))
            .thenThrow(new RuntimeException("junit-test"));
        Assertions.assertEquals(-2, apiService.restartDeployment(API_MOCK));
    }

    @Test
    void getApiListByKeyword() {
        // 对于存在的关键词，返回结果应该不为空
        // 不存在的关键词应该返回空的结果
        Assertions.assertTrue(!apiService.getApiListByKeyword("demo").getApiOutlines().isEmpty());
        Assertions.assertTrue(apiService.getApiListByKeyword("wbq").getApiOutlines().isEmpty());
    }
}