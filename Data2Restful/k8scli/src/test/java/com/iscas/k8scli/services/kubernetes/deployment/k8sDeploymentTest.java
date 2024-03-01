package com.iscas.k8scli.services.kubernetes.deployment;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ListIterator;

@RunWith(SpringRunner.class)
@ActiveProfiles("t14s")
@SpringBootTest
class k8sDeploymentTest {

    @Autowired
    private K8sDeployment k8sDeployment;

    @Test
    void listK8sDeployment() {
        V1DeploymentList v1DeploymentList = k8sDeployment.listK8sDeployment();
        ListIterator<V1Deployment> v1DeploymentListIterator = v1DeploymentList.getItems().listIterator();
        while (v1DeploymentListIterator.hasNext()) {
            V1Deployment next = v1DeploymentListIterator.next();
            System.out.println(next);
        }

        System.out.println();
    }

    @Test
    void deleteK8sDeployment() throws IOException, ApiException {
        k8sDeployment.deleteK8sDeployment("sql1test");
    }

}