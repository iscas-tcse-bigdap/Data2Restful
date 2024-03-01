package com.iscas.k8scli.services.kubernetes.svc;

import com.iscas.k8scli.pojo.AppInfo;
import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.kubernetes.common.K8sCommon;
import com.iscas.k8scli.utils.common.FileUtil;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: K8sSVC
 * @Description:
 * @Author: wzc
 * @Date: 2023/8/6 23:00
 */
@Service
public class K8sSVC {

    @Autowired
    private K8sCommon k8sCommon;

    private String pattern;

    private static final int NODE_PORT = 31000;
    /**
     * svc 配置文件选择
     */
    @PostConstruct
    public void postConstruct() {
        pattern = FileUtil.getResourceTemplate("/k8sYamlTemplate/svc/base_svc.yaml");
    }

    /***
     * @Author wzc
     * @Description 通过配置创建svc
     * @Date 23:35 2023/8/10
     * @Param [info]
     * @return com.iscas.k8scli.response.Result
     **/
    public Result createK8SService(AppInfo info) {
        String namespace = k8sCommon.getNamespace();
        Map<String, String> matchLabels = new HashMap<>();
        matchLabels.put("app", info.getName());

        List<V1ServicePort> portList = new ArrayList<>();
        V1ServicePort port = new V1ServicePort();
        port.nodePort(NODE_PORT);
        port.port(info.getContainerPort());
        port.name("flask-port");
        port.setProtocol("TCP");
        IntOrString targt = new IntOrString(info.getContainerPort());

        port.setTargetPort(targt);
        portList.add(port);

        V1Service result;
        //V1Service body = serviceService.createV1Service(serviceDTO);
        V1Service body2 = new V1Service();
        body2.setApiVersion("v1");
        body2.setKind("Service");

        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName(info.getName());
        meta.setNamespace(namespace);
        meta.setLabels(matchLabels);
        body2.setMetadata(meta);
        V1ServiceSpec spec = new V1ServiceSpec();
        spec.setType("NodePort");
        spec.setSelector(matchLabels);
        spec.setPorts(portList);
        body2.setSpec(spec);
        System.out.println(body2.toString());

        try {
            CoreV1Api apiInstance = k8sCommon.getCoreApiInstanceWithConfig();
            result = apiInstance.createNamespacedService(namespace, body2, "true", null, null);
            System.out.println(result);
        } catch (ApiException e) {
            e.printStackTrace();
            return Result.failed(e.getCode(), e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(e.getMessage());
        }

        return Result.succeed(result.toString());
    }

    /***
     * @Author wzc
     * @Description 使用yaml创建svc
     * @Date 23:35 2023/8/10
     * @Param [apiName]
     * @return com.iscas.k8scli.response.Result
     **/
    public Result createK8sFromYaml(String groupName) throws IOException, ApiException {
        CoreV1Api apiInstance = k8sCommon.getCoreApiInstanceWithConfig();
        // 检查是否该 service 是否存在
        V1ServiceList serviceList = apiInstance.listNamespacedService("default", null, null, null, null,
            null, null, null, null, null, null);
        boolean created = false;
        for (V1Service service : serviceList.getItems()) {
            if (service.getMetadata().getName().equals(groupName + "-svc")) {
                created = true;
            }
        }
        if (!created) {
            String template = pattern.replace("${groupName}", groupName);
            V1Service svc = Yaml.loadAs(template, V1Service.class);
            V1Service createResult = apiInstance.createNamespacedService("default", svc, null, null, null);
        }
        return Result.succeed("succeed");
    }


    /***
     * @Author wzc
     * @Description 删除 svc
     * @Date 23:34 2023/8/10
     * @Param [name]
     * @return io.kubernetes.client.openapi.models.V1Status
     **/
    public V1Status deleteK8sService(String name) throws ApiException, IOException {
        V1Status result;
        String namespace = k8sCommon.getNamespace();
        CoreV1Api apiInstance = k8sCommon.getCoreApiInstanceWithConfig();
        result = apiInstance.deleteNamespacedService(name, namespace, "true", null, null, null, null, null);
        System.out.println(result);
        return result;
    }

    /**
     * 获取svc
     */
    public V1Service getK8SService(String name) {
        V1Service result = null;
        String namespace = k8sCommon.getNamespace();
        CoreV1Api apiInstance = null;
        try {
            apiInstance = k8sCommon.getCoreApiInstanceWithConfig();
            result = apiInstance.readNamespacedService(name, namespace, "true");
        } catch (IOException e) {
            return result;
        } catch (ApiException e) {
            return result;
        }
        return result;
    }

    /**
     * 获取svc列表
     */
    public V1ServiceList listK8sService() {
        V1ServiceList result;
        String namespace = k8sCommon.getNamespace();
        try {
            CoreV1Api apiInstance = k8sCommon.getCoreApiInstanceWithConfig();
            result = apiInstance.listNamespacedService(namespace, null, null, null, null, null,
                null, null, null, null, null);
        } catch (ApiException e) {
            return null;
        } catch (IOException e) {
            throw null;
        }

        return result;
    }
}
