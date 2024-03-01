package com.iscas.k8scli.services.api.impl;

import com.iscas.k8scli.services.api.ApiService;
import io.kubernetes.client.openapi.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("t14s")
@SpringBootTest
class ApiServiceImplTest {

    @Autowired
    private ApiService apiService;

    @Test
    void publicSqlApiService() throws IOException, ApiException {

        apiService.publicSqlApiService("sql1test", "11", "datacenter");
    }

    @Test
    void deleteApi() throws IOException, ApiException {
        apiService.delSqlApiService("sql1test");
    }
}