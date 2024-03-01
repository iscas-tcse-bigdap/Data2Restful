package com.iscas.k8scli.services.kubernetes.deployment;

import com.iscas.k8scli.pojo.AppInfo;
import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.kubernetes.common.K8sCommon;
import com.iscas.k8scli.utils.common.FileUtil;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ContainerPort;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1DeploymentSpec;
import io.kubernetes.client.openapi.models.V1EnvVar;
import io.kubernetes.client.openapi.models.V1LabelSelector;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
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
 * @ClassName: k8sDeployment
 * @Description:
 * @Author: wzc
 * @Date: 2023/7/25 15:01
 */
@Service
public class K8sDeployment {

    @Autowired
    private K8sCommon k8sCommon;

    private String pattern;

    /**
     * 选择deployment配置文件
     */
    @PostConstruct
    public void postConstruct() {
        pattern = FileUtil.getResourceTemplate("/k8sYamlTemplate/deployments/base_deployment.yaml");
    }

    /***
     * @Author wzc
     * @Description 构建api服务的Deployment
     * pod名称
     * 端口号
     *
     * @Date 15:02 2023/7/25
     * @Param []
     * @return io.kubernetes.client.openapi.models.V1Deployment
     **/
    public V1Deployment buildApiDeployment() {

        return new V1Deployment();
    }

    /***
     * @Author wzc
     * @Description 获取Deployment清单
     * @Date 22:50 2023/8/6
     * @Param []
     * @return io.kubernetes.client.openapi.models.V1DeploymentList
     **/
    public V1DeploymentList listK8sDeployment() {
        V1DeploymentList result;
        try {
            AppsV1Api apiInstance = k8sCommon.getApiInstanceWithConfig();
            result = apiInstance.listNamespacedDeployment(k8sCommon.getNamespace(), null, null,
                null, null, null, null, null, null, null, null);
        } catch (ApiException | IOException e) {
            return null;
        }

        return result;
    }

    /**
     * 获取deployment
     */
    public V1Deployment getK8sDeployment(String name) {
        V1Deployment result = null;

        AppsV1Api apiInstance = null;
        try {
            apiInstance = k8sCommon.getApiInstanceWithConfig();
            System.out.println(name);
            result = apiInstance.readNamespacedDeployment(name, k8sCommon.getNamespace(), "");
        } catch (IOException e) {
//            e.printStackTrace();
            return null;
        } catch (ApiException e) {
            System.out.println("没有Deployment");
//            e.printStackTrace();
            return null;
        } finally {
            return result;
        }
    }


    /***
     * @Author wzc
     * @Description 删除Deployment
     * @Date 22:49 2023/8/6
     * @Param [name]
     * @return io.kubernetes.client.openapi.models.V1Status
     **/
    public V1Status deleteK8sDeployment(String name) throws ApiException, IOException {
        V1Status result;
        AppsV1Api apiInstance = k8sCommon.getApiInstanceWithConfig();
        result = apiInstance.deleteNamespacedDeployment(name, k8sCommon.getNamespace(), "true", null, null, null,
            null, null);
        System.out.println(result);

        return result;
    }

    /***
     * @Author wzc
     * @Description 创建Deployment
     * @Date 22:52 2023/8/6
     * @Param [info]
     * @return com.iscas.k8scli.response.Result
     **/
    public Result createK8sDeployment(AppInfo info) {
        V1Deployment result;
        String namespace = k8sCommon.getNamespace();

        // labels
        Map<String, String> matchLabels = new HashMap<>();
        matchLabels.put("app", info.getName());

        V1Deployment body2 = new V1Deployment();
        body2.setApiVersion("apps/v1");
        body2.setKind("Deployment");
        //meta
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName(info.getName());
        meta.setNamespace(namespace);
        body2.setMetadata(meta);
        //spec
        V1DeploymentSpec spec = new V1DeploymentSpec();
        spec.setReplicas(info.getInitInstanceNum());
        V1LabelSelector selector = new V1LabelSelector();
        selector.setMatchLabels(matchLabels);
        spec.setSelector(selector);

        V1PodTemplateSpec spec2 = new V1PodTemplateSpec();
        V1ObjectMeta meta2 = new V1ObjectMeta();
        meta2.setLabels(matchLabels);
        spec2.setMetadata(meta2);
        V1PodSpec podSpec = new V1PodSpec();
        List<V1Container> list = new ArrayList<>();

        V1Container container = new V1Container();
        container.setName(info.getName());
        container.image(info.getImage());
        container.setImagePullPolicy("IfNotPresent");

        List<V1ContainerPort> portList = buildPort(info); //容器端口处理
        container.ports(portList);


        List<V1EnvVar> envList = buildEnv(info); //构建环境变量

        container.setEnv(envList);
        list.add(container);
        podSpec.setContainers(list);

        spec2.setSpec(podSpec);
        spec.setTemplate(spec2);
        body2.setSpec(spec);

        try {
            AppsV1Api apiInstance = k8sCommon.getApiInstanceWithConfig();
            result = apiInstance.createNamespacedDeployment(
                    namespace,
                    body2,
                    "true",
                    null,
                    null);
            System.out.println(result.toString());
        } catch (ApiException e) {
            e.printStackTrace();
            return Result.failed(e.getCode(), e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(e.getMessage());
        }

        return Result.succeed("succeed"); //gson.toJson(result)
    }

    /**
     * 基于yaml文件的配置创建deployment
     */
    public Result createK8sFromYaml(String apiName, String groupName, String srcName) throws IOException, ApiException {
        AppsV1Api apiInstance = k8sCommon.getApiInstanceWithConfig();
        String template = pattern.replace("${apiName}", apiName)
                .replace("${groupName}", groupName)
                .replace("${configMap}", srcName + "-cm")
                .replace("${secret}", srcName + "-secret");
        V1Deployment deployment = Yaml.loadAs(template, V1Deployment.class);
        V1Deployment createResult = apiInstance.createNamespacedDeployment("default", deployment, null, null, null);
        return Result.succeed("succeed");
    }

    /**
     * @Deprecated 基于Yaml文件的哦欸之创建deployment，最初设计用于测试新方案，目前已不再使用
     */
    public Result createDeploymentFromYamlInSvc(String srcName, String groupName) throws IOException, ApiException {
        AppsV1Api apiInstance = k8sCommon.getApiInstanceWithConfig();
        V1DeploymentList deploymentList = apiInstance.listNamespacedDeployment("default", null, null, null, null,
            null, null, null, null, null, null);
        for (V1Deployment deployment : deploymentList.getItems()) {
            if (deployment.getMetadata().getName().equals(groupName)) {
                apiInstance.deleteNamespacedDeployment(groupName, k8sCommon.getNamespace(), "true", null, null, null,
                    null, null);
            }
        }
        String template = pattern.replace("${groupName}", groupName)
                .replace("${configMap}", srcName + "-cm")
                .replace("${secret}", srcName + "-secret");
        V1Deployment deployment = Yaml.loadAs(template, V1Deployment.class);
        V1Deployment createResult = apiInstance.createNamespacedDeployment("default", deployment, null, null, null);
        return Result.succeed("succeed");
    }

    /***
     * @Author wzc
     * @Description 配置Port
     * @Date 22:54 2023/8/6
     * @Param [info]
     * @return java.util.List<io.kubernetes.client.openapi.models.V1ContainerPort>
     **/
    public List<V1ContainerPort> buildPort(AppInfo info) {
        // ports
        List<V1ContainerPort> portList = new ArrayList<>();
        V1ContainerPort port = new V1ContainerPort();
        port.setContainerPort(info.getContainerPort());
        portList.add(port);
        return portList;
    }


    /***
     * @Author wzc
     * @Description 配置环境变量
     * @Date 22:54 2023/8/6
     * @Param [info]
     * @return java.util.List<io.kubernetes.client.openapi.models.V1EnvVar>
     **/
    public List<V1EnvVar> buildEnv(AppInfo info) {
        List<V1EnvVar> envList = new ArrayList<>();
        V1EnvVar env = new V1EnvVar();
        env.setName("code");
        env.setValue(info.getCode());
        envList.add(env);

        V1EnvVar env2 = new V1EnvVar();
        env2.setName("s_port");
        env2.setValue(String.valueOf(info.getContainerPort()));
        envList.add(env2);
        return envList;
    }
}
