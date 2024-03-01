package com.iscas.k8scli.services.kubernetes.impl;


import com.iscas.k8scli.pojo.AppInfo;
import com.iscas.k8scli.pojo.InstanceInfo;
import com.iscas.k8scli.pojo.ServiceInfo;
import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.app.AppService;
import com.iscas.k8scli.services.kubernetes.K8sService;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ContainerPort;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1DeploymentSpec;
import io.kubernetes.client.openapi.models.V1EnvVar;
import io.kubernetes.client.openapi.models.V1LabelSelector;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
import io.kubernetes.client.openapi.models.V1Scale;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: K8sServiceImpl
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/20 11:35
 */
@Service
public class K8sServiceImpl implements K8sService {
    @Resource
    private AppService dbService;

    @Value("${zc.k8s.base-url}")
    private String url;

    @Value("${zc.k8s.token}")
    private String token;

    @Value("${zc.k8s.namespace:default}")
    private String namespace;

    @Value("${zc.k8s.config.file}")
    private String k8sConfig;
    private static final int NODE_PORT = 31000;
    private static final int TIME_PLUS_HOUR = 8;
    private AppsV1Api getApiInstance() {
        ApiClient client = new ClientBuilder()
            .setBasePath(url)
            .setVerifyingSsl(false)
            .setAuthentication(new AccessTokenAuthentication(token))
            .build();

        Configuration.setDefaultApiClient(client);
        return new AppsV1Api(client);
    }

    private AppsV1Api getApiInstanceWithConfig() throws IOException {
        InputStream configPath = K8sServiceImpl.class.getClassLoader().getResourceAsStream(k8sConfig);
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        return new AppsV1Api(client);
    }

    private AppsV1Api getApiInstanceWithConfig(InputStream configPath) throws IOException {
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        return new AppsV1Api(client);
    }

    private CoreV1Api getCoreApiInstance() {
        ApiClient client = new ClientBuilder()
            .setBasePath(url)
            .setVerifyingSsl(false)
            .setAuthentication(new AccessTokenAuthentication(token))
            .build();

        Configuration.setDefaultApiClient(client);
        return new CoreV1Api(client);
    }

    private CoreV1Api getCoreApiInstanceWithConfig() throws IOException {
        InputStream configPath = K8sServiceImpl.class.getClassLoader().getResourceAsStream(k8sConfig);
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        return new CoreV1Api(client);
    }

    private CoreV1Api getCoreApiInstanceWithConfig(InputStream configPath) throws IOException {
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        return new CoreV1Api(client);
    }

    private Result createK8SService(AppInfo info) {
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
            CoreV1Api apiInstance = getCoreApiInstanceWithConfig();
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

    private V1Status deleteK8sService(String name) throws ApiException, IOException {
        V1Status result;

        CoreV1Api apiInstance = getCoreApiInstanceWithConfig();
        result = apiInstance.deleteNamespacedService(name, namespace, "true", null, null, null, null, null);
        System.out.println(result);

        return result;
    }

    private V1Service getK8SService(String name) {
        V1Service result = null;

        CoreV1Api apiInstance = null;
        try {
            apiInstance = getCoreApiInstanceWithConfig();
            result = apiInstance.readNamespacedService(name, namespace, "true");
        } catch (IOException e) {
            return result;
        } catch (ApiException e) {
            return result;
        }
        return result;
    }

    private V1ServiceList listK8sService() {
        V1ServiceList result;

        try {
            CoreV1Api apiInstance = getCoreApiInstanceWithConfig();
            result = apiInstance.listNamespacedService(namespace, null, null, null, null, null, null, null, null,
                null, null);
        } catch (ApiException e) {
            return null;
        } catch (IOException e) {
            throw null;
        }

        return result;
    }

    /**
     * buildPort
     */
    public List<V1ContainerPort> buildPort(AppInfo info) {
        // ports
        List<V1ContainerPort> portList = new ArrayList<>();
        V1ContainerPort port = new V1ContainerPort();
        port.setContainerPort(info.getContainerPort());
        portList.add(port);
        return portList;
    }


    /**
     * buildEnv
     */
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


    private Result createK8sDeployment(AppInfo info) {
        V1Deployment result;

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
            AppsV1Api apiInstance = getApiInstanceWithConfig();
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

    private V1Status deleteK8sDeployment(String name) throws ApiException, IOException {
        V1Status result;

        AppsV1Api apiInstance = getApiInstanceWithConfig();
        result = apiInstance.deleteNamespacedDeployment(name, namespace, "true", null, null, null, null, null);
        System.out.println(result);

        return result;
    }

    private V1Deployment getK8sDeployment(String name) {
        V1Deployment result = null;

        AppsV1Api apiInstance = null;
        try {
            apiInstance = getApiInstanceWithConfig();
            System.out.println(name);
            result = apiInstance.readNamespacedDeployment(name, namespace, "");
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

    private V1DeploymentList listK8sDeployment() {
        V1DeploymentList result;

        try {
            AppsV1Api apiInstance = getApiInstanceWithConfig();
            result = apiInstance.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null,
                null, null);
        } catch (ApiException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return result;
    }

    private V1PodList listK8sPod(String name) throws ApiException, IOException {
        CoreV1Api apiCore = getCoreApiInstanceWithConfig();
        V1PodList list = null;

        list = apiCore.listNamespacedPod(namespace, "true", null, null, null, "app=" + name, null, null, null, null,
            null);

        return list;
    }

    private V1PodList getK8sPods() throws ApiException, IOException {
        V1PodList result = null;

        CoreV1Api apiCore = getCoreApiInstanceWithConfig();
        result = apiCore.listNamespacedPod(namespace, "true", null, null, null, null, null, null, null, null, null);

        return result;
    }

    private Result updateK8sScale(String name, int num) {
        V1Scale result;

        // 更新副本的json串
        String jsonPatchStr = "[{\"op\":\"replace\",\"path\":\"/spec/replicas\", \"value\": " + num + " }]";
        V1Patch body = new V1Patch(jsonPatchStr);
        V1Deployment v1Deployment;

        try {
            AppsV1Api apiInstance = getApiInstanceWithConfig();
            v1Deployment = apiInstance.patchNamespacedDeployment(name,
                namespace,
                body,
                null,
                null,
                null,
                null);
        } catch (ApiException e) {
            System.out.println(e.getMessage());
            return Result.failed(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(e.getMessage());

        }

        return Result.succeed(v1Deployment.toString());
    }

    /**
     * 更新app scale
     * @param name
     * @param num
     * @return
     */
    public Result scaleApp(String name, int num) {
        AppInfo info = dbService.getAppInfo(name);

        if (info == null) {
            return Result.failed("不存在该应用程序信息");
        }

        return updateK8sScale(name, num);
    }

    /**
     * 重写runApp方法
     */
    @Override
    public Result runApp(String name) {
        // 获取应用信息
        AppInfo info = dbService.getAppInfo(name);

        if (info == null) {
            return Result.failed("不存在该应用程序信息");
        }

        V1Service svc = null;
        V1Deployment dep = null;

        // 获取已有Service
        svc = getK8SService(name);

        dep = getK8sDeployment(name);
        Result rel = Result.failed("");
        if (dep == null) {
            rel = createK8sDeployment(info);
        }

        if (rel.isSuccess() && svc == null) {   //没有服务则需要创建
            return createK8SService(info);
        }

        return rel;

    }

    /**
     * get app info
     */
    public Result getAppInfo(String name) {
        // 获取应用信息
        AppInfo info = dbService.getAppInfo(name);

        if (info == null) {
            return Result.failed("不存在该应用程序信息");
        }

        ServiceInfo sinfo = new ServiceInfo();
        sinfo.setAppInfo(info);

        try {
            V1Service rel = getK8SService(name);

            sinfo.setStatus("running");
            sinfo.setUID(rel.getMetadata().getUid());
            sinfo.setStartTime(rel.getMetadata().getCreationTimestamp().toLocalDateTime().plusHours(TIME_PLUS_HOUR)
                .toString());

            List<V1Pod> list = listK8sPod(name).getItems();
            System.out.println(list);

            List<InstanceInfo> infos = new ArrayList<>();

            for (V1Pod pod : list) {
                InstanceInfo inst = new InstanceInfo();

                inst.setName(pod.getMetadata().getName());
                inst.setCreateTime(pod.getMetadata().getCreationTimestamp().toLocalDateTime().plusHours(TIME_PLUS_HOUR)
                    .toString());
                inst.setUid(pod.getMetadata().getUid());
                inst.setInnerIp(pod.getStatus().getPodIP());

                infos.add(inst);
            }

            sinfo.setInstanceNum(infos.size());
            sinfo.setInstances(infos);
        } catch (ApiException e) {
            sinfo.setStatus("not running");

            return Result.succeed(sinfo);
        } catch (IOException e) {
            return Result.succeed(sinfo);
        }

        return Result.succeed(sinfo);
    }

    /**
     * stop app
     */
    public Result stopApp(String name) {
        // 获取应用信息
        AppInfo info = dbService.getAppInfo(name);

        if (info == null) {
            return Result.failed("不存在该应用程序信息");
        }

        V1Status status;

        try {
            AppsV1Api apiApps = getApiInstanceWithConfig();
            CoreV1Api apiCore = getCoreApiInstanceWithConfig();

            status = deleteK8sDeployment(name);
            status = deleteK8sService(name);
        } catch (ApiException e) {
            System.out.println(e.getMessage());
            return Result.failed(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(e.getMessage());
        }

        return Result.succeed("succeed");
    }

    /**
     * app info list
     */
    public Result listAppInfo() {
        List<AppInfo> apps = dbService.getAppInfos();
        List<ServiceInfo> res = new ArrayList<>();

        for (AppInfo app : apps) {
            ServiceInfo info = (ServiceInfo) getAppInfo(app.getName()).getData();
            res.add(info);
        }

        return Result.succeed(res);
    }


    public static void main(String[] args) throws IOException, ApiException {
        K8sServiceImpl u = new K8sServiceImpl();
        InputStream configPath = K8sServiceImpl.class.getClassLoader().getResourceAsStream("kubernetesconfig_t14s");
//        CoreV1Api coreApiInstanceWithConfig = u.getCoreApiInstanceWithConfig(configPath);
////        CoreV1Api coreApiInstanceWithConfig = u.getCoreApiInstanceWithConfig(configPath);
//        V1PodList result = coreApiInstanceWithConfig.listNamespacedPod("default", "true", null, null, null, "",
//        null, null, null, null, null);
//        System.out.println(result.getItems());

        AppsV1Api apiInstanceWithConfig = u.getApiInstanceWithConfig(configPath);
        V1Deployment v1Deployment = apiInstanceWithConfig.readNamespacedDeployment("flaskdemo", "default", "");
        System.out.println(v1Deployment);
    }
}
