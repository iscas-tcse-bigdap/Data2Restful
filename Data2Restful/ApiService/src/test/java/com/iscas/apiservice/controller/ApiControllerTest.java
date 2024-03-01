package com.iscas.apiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.apiservice.pojo.Api;
import com.iscas.apiservice.pojo.controllerToWeb.ApiAllDetail;
import com.iscas.apiservice.pojo.controllerToWeb.ApiListAndItsGroup;
import com.iscas.apiservice.pojo.controllerToWeb.ApiOutline;
import com.iscas.apiservice.pojo.webToController.ApiAndParam;
import com.iscas.apiservice.pojo.webToController.BatchImport;
import com.iscas.apiservice.service.impl.ApiServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * @author wbq
 * @version 1.0
 * @title ApiControllerTest
 * @description
 * @create 2023/11/27 15:04
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("ApiController-test")
@Rollback
class ApiControllerTest {

    private static final ApiAndParam API_AND_PARAM_MOCK = new ApiAndParam(1, 1, 1, "junit-test", "junit-test", "junit" +
        "-test-code", "junit-test", 1);
    private static final Api API_MOCK = new Api(API_AND_PARAM_MOCK);
    private static final ApiOutline API_OUTLINE_MOCK = new ApiOutline(API_MOCK);
    private static final ApiAllDetail API_ALL_DETAIL_MOCK_OUTPUT = new ApiAllDetail(API_MOCK);
    private static final ApiListAndItsGroup GET_API_LIST_MOCK_OUTPUT =
        new ApiListAndItsGroup(new ArrayList<ApiOutline>() {{
        add(API_OUTLINE_MOCK);
    }}, new HashSet<String>());
    private static final ArrayList<Api> GET_API_LIST_BY_GROUP_ID_MOCK_OUTPUT = new ArrayList<Api>() {{
        add(API_MOCK);
    }};

    @InjectMocks
    private ApiController apiController;

    @Mock
    private ApiServiceImpl apiService;

    @BeforeEach
    void setMockOutput() {
        when(apiService.getApiInfoByApiId(anyInt())).thenReturn(API_ALL_DETAIL_MOCK_OUTPUT);
        when(apiService.createApi(any())).thenReturn(1);
        when(apiService.getApiList()).thenReturn(GET_API_LIST_MOCK_OUTPUT);
//        when(apiService.getApiListByGroupId(anyInt())).thenReturn(GET_API_LIST_BY_GROUP_ID_MOCK_OUTPUT);
        when(apiService.getCodeByApiId(anyInt())).thenReturn(API_MOCK.getCode());
        when(apiService.updateApi(any())).thenReturn(1);
        when(apiService.deleteApi(anyInt())).thenReturn(1);
        when(apiService.publishApi(any())).thenReturn(1);
        when(apiService.terminateApi(any())).thenReturn(1);
        when(apiService.getApiListByKeyword(any())).thenReturn(GET_API_LIST_MOCK_OUTPUT);
        when(apiService.createApiAndAddParams(any(), any())).thenReturn(1);
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @AfterEach
    void tearDown() {
    }


    @Test
    @Transactional
    void createApi() {
        assertTrue(apiController.createApi(new Api().setNameAndReturn("junit-test")).getData().containsKey("ID"));
    }

    @Test
    void getApiInfoByApiId() {
        assertEquals(apiController.getApiInfoByApiId(1).getData().get("api"), API_ALL_DETAIL_MOCK_OUTPUT,
            () -> "Expected API: " + API_ALL_DETAIL_MOCK_OUTPUT + ", and the test got: " + apiController.getApiInfoByApiId(1).getData().get("api"));
    }

    @Test
    void getApiList() {
        assertEquals(apiController.getApiList().getData().get("apiOutlineAndGroup"), GET_API_LIST_MOCK_OUTPUT,
            () -> "Expected list: " + GET_API_LIST_MOCK_OUTPUT + ", and the test got: " + apiController.getApiList().getData().get("apiOutlineAndGroup"));
    }

    @Test
    void getApiListByGroupId() {
        Assertions.assertNotNull(apiController.getApiListByGroupId(1).getData().get("apiList"));
//        assertEquals(apiController.getApiListByGroupId(1).getData().get("apiList"),
//        GET_API_LIST_BY_GROUP_ID_MOCK_OUTPUT,
//            () -> "Expected list: " + GET_API_LIST_BY_GROUP_ID_MOCK_OUTPUT + ", and the test got: " + apiController
//            .getApiListByGroupId(1).getData().get("apiList"));
    }

    @Test
    void getCodeByApiId() {
        assertEquals(apiController.getCodeByApiId(1).getData().get("code"), API_ALL_DETAIL_MOCK_OUTPUT.getCode(),
            () -> "Expected code: " + API_ALL_DETAIL_MOCK_OUTPUT.getCode() + ", and the test got: " + apiController.getCodeByApiId(1).getData().get("code"));
    }

    @Test
    @Transactional
    void updateApi() {
        assertTrue(apiController.updateApi(new Api().setNameAndReturn("junit-test")).isSuccess());
    }

    @Test
    @Transactional
    void deleteApi() {
        assertTrue(apiController.deleteApi(1).isSuccess());
    }

    @Test
    @Transactional
    void publishApi() {
        assertTrue(apiController.publishApi(new Api().setNameAndReturn("junit-test")).isSuccess());
    }

    @Test
    @Transactional
    void terminateApi() {
        assertTrue(apiController.terminateApi(new Api().setNameAndReturn("junit-test")).isSuccess());
    }

    @Test
    void filterListByKeyword() {
        assertEquals(apiController.filterListByKeyword("junit-test").getData().get("apiListWithKeyword"),
            GET_API_LIST_MOCK_OUTPUT,
            () -> "Expected list: " + GET_API_LIST_MOCK_OUTPUT + ", and the test got: " + apiController.filterListByKeyword("junit-test").getData().get("apiList"));
    }

    @Test
    void createManyApi() {
        assertTrue(apiController.createManyApi(new ArrayList<BatchImport>()).isSuccess());
    }

    // 将对象转换为JSON字符串的实用方法
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}