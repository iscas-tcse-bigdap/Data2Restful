package com.iscas.k8scli.controller;

import com.iscas.k8scli.pojo.DTO.CreateDeploymentInfo;
import com.iscas.k8scli.pojo.DTO.CreateHpaInfo;
import com.iscas.k8scli.pojo.DTO.CreateIngressInfo;
import com.iscas.k8scli.pojo.DataSourceInfo;
import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.api.ApiService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ClassName: ApiServiceController
 * @Description:
 * @Author: wzc
 * @Date: 2023/7/25 14:34
 */

@RestController
@RequestMapping("/api/k8s")
public final class ApiServiceController {

    @Autowired
    private ApiService apiService;


    @GetMapping(value = "/public_api")
    public Result publicApi(@RequestParam("name") String name,
                            @RequestParam("groupName") String groupName,
                            @RequestParam("srcName") String srcName) throws IOException, ApiException {
        Result result = apiService.publicSqlApiService(name, groupName, srcName);
        return result;
    }

    @GetMapping(value = "/del")
    public Result delApi(@RequestParam("name") String name,
                         @RequestParam("id") String id) throws IOException, ApiException {
        Result result = apiService.delSqlApiService(name);
        return result;
    }

    @GetMapping("/list")
    public Result apiList() throws IOException, ApiException {
        Result result = apiService.listSqlApi();
        return result;
    }

    @RequestMapping(path = "/listPods")
    public V1PodList listPods() throws IOException, ApiException {
        CoreV1Api api = new CoreV1Api();
        V1PodList list = api.listPodForAllNamespaces(null, null, null,
            null, null, null, null, null, null, false);
        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
        return list;
    }

    @RequestMapping(path = "/updateReplicaNum")
    public Result updateReplicaNum(@RequestParam("name") String name, @RequestParam("num") int num)
        throws IOException, ApiException {
        return apiService.updateReplicaNum(name, num);
    }

    @RequestMapping(path = "/createAutoAdjustReplica", method = RequestMethod.POST)
    public Result createAutoAdjustReplica(@RequestBody CreateHpaInfo createHpaInfo) throws IOException, ApiException {
        return apiService.setAutoAdjustReplica(createHpaInfo.getGroupName(), createHpaInfo.getMinValue(),
            createHpaInfo.getMaxValue());
    }

    @RequestMapping(path = "/updateAutoAdjustReplicaNum", method = RequestMethod.PUT)
    public Result updateAutoAdjustReplicaNum(@RequestBody CreateHpaInfo createHpaInfo) throws IOException {
        return apiService.updateAutoAdjustReplicaNum(createHpaInfo.getGroupName(), createHpaInfo.getMinValue(),
            createHpaInfo.getMaxValue());
    }

    @RequestMapping(path = "/deleteAutoAdjustReplica", method = RequestMethod.DELETE)
    public Result deleteAutoAdjustReplica(@RequestParam("groupName") String groupName) throws IOException {
        return apiService.deleteAutoAdjustReplica(groupName);
    }

    @RequestMapping(path = "/createConfigMap", method = RequestMethod.POST)
    public Result createConfigMap(@RequestBody DataSourceInfo dataSourceInfo) throws IOException, ApiException {
        return apiService.createConfigMap(dataSourceInfo);
    }

    @RequestMapping(path = "/deleteConfigMap", method = RequestMethod.DELETE)
    public Result deleteConfigMap(@RequestParam("srcName") String srcName) throws IOException, ApiException {
        return apiService.deleteConfigMap(srcName);
    }

    @RequestMapping(path = "/updateConfigMap")
    public Result updateConfigMap(@RequestBody DataSourceInfo dataSourceInfo) throws IOException, ApiException {
        return apiService.updateConfigMap(dataSourceInfo);
    }

    @RequestMapping(path = "/createIngress", method = RequestMethod.POST)
    public String createIngress(@RequestBody CreateIngressInfo createIngressInfo) throws IOException, ApiException {
        return apiService.createIngress(createIngressInfo);
    }

    @RequestMapping(path = "/deleteIngress", method = RequestMethod.DELETE)
    public String deleteIngress(@RequestParam("groupName") String groupName) throws IOException, ApiException {
        return apiService.deleteIngress(groupName);
    }

    @RequestMapping(path = "/createDeployment", method = RequestMethod.POST)
    public Result createDeployment(@RequestBody CreateDeploymentInfo createDeploymentInfo)
        throws IOException, ApiException {
        return apiService.createDeployment(createDeploymentInfo);
    }

    @RequestMapping(path = "/deleteDeployment", method = RequestMethod.DELETE)
    public Result deleteDeployment(@RequestParam("name") String name) throws IOException, ApiException {
        return apiService.deleteDeployment(name);
    }
}
