package com.iscas.k8scli.services.api;

import com.iscas.k8scli.pojo.DTO.CreateDeploymentInfo;
import com.iscas.k8scli.pojo.DTO.CreateIngressInfo;
import com.iscas.k8scli.pojo.DataSourceInfo;
import com.iscas.k8scli.response.Result;
import io.kubernetes.client.openapi.ApiException;

import java.io.IOException;

public interface ApiService {

    Result publicSqlApiService(String apiName, String groupName, String srcName) throws IOException, ApiException;

    Result delSqlApiService(String apiName) throws IOException, ApiException;

    Result listSqlApi() throws IOException, ApiException;

    Result updateReplicaNum(String groupName, int num) throws IOException, ApiException;

    Result setAutoAdjustReplica(String groupName, int minNum, int maxNum) throws IOException, ApiException;

    Result updateAutoAdjustReplicaNum(String groupName, int minNum, int maxNum) throws IOException;

    Result deleteAutoAdjustReplica(String groupName) throws IOException;

    Result createConfigMap(DataSourceInfo dataSourceInfo) throws IOException, ApiException;

    Result deleteConfigMap(String srcName) throws IOException, ApiException;

    Result updateConfigMap(DataSourceInfo dataSourceInfo) throws IOException, ApiException;

    Result createDeployment(CreateDeploymentInfo createDeploymentInfo) throws IOException, ApiException;

    Result deleteDeployment(String name) throws IOException, ApiException;

    String createIngress(CreateIngressInfo createIngressInfo) throws IOException, ApiException;

    String deleteIngress(String groupName) throws IOException, ApiException;

}
