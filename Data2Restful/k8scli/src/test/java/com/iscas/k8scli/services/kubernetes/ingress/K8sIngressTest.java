package com.iscas.k8scli.services.kubernetes.ingress;

import io.kubernetes.client.openapi.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@ActiveProfiles("t14s")
@SpringBootTest
class K8sIngressTest {

    @Autowired
    private K8sIngressService k8sIngress;

    @Test
    void createIngress() throws IOException, ApiException {
        k8sIngress.testCreate();
    }


    @Test
    void listIngress() throws IOException, ApiException {
        k8sIngress.listIngress();
    }

    @Test
    void delIngress() throws IOException, ApiException {
        k8sIngress.deleteIngress("sql1test-ingress");
    }

    @Test
    void createK8sFromYaml() throws IOException, ApiException {
        k8sIngress.createK8sFromYaml("sql1test");
    }
}