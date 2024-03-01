package com.iscas.k8scli.services.api.impl;

import com.iscas.k8scli.pojo.DTO.CreateDeploymentInfo;
import com.iscas.k8scli.pojo.DTO.CreateIngressInfo;
import com.iscas.k8scli.pojo.DataSourceInfo;
import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.api.ApiService;
import com.iscas.k8scli.services.kubernetes.common.K8sCommon;
import com.iscas.k8scli.services.kubernetes.deployment.K8sDeployment;
import com.iscas.k8scli.services.kubernetes.ingress.K8sIngressService;
import com.iscas.k8scli.services.kubernetes.svc.K8sSVC;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.AutoscalingV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1CrossVersionObjectReference;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1HorizontalPodAutoscaler;
import io.kubernetes.client.openapi.models.V1HorizontalPodAutoscalerSpec;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Secret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: ApiServiceImpl
 * @Description:
 * @Author: wzc
 * @Date: 2023/8/9 23:18
 */
@Service
public class ApiServiceImpl implements ApiService {


    @Autowired
    private K8sSVC k8sSVC;

    @Autowired
    private K8sDeployment k8sDeployment;

    @Autowired
    private K8sIngressService k8sIngress;

    @Autowired
    private K8sCommon k8sCommon;
    private static final int DEFAULT_CPU_PER = 60;

    private static final int NOT_FOUND = 404;
    /**
     * @Deprecated 发布接口。已弃用！！！
     */
    public Result publicSqlApiService(String apiName, String groupName, String srcName)
        throws IOException, ApiException {
        k8sDeployment.createK8sFromYaml(apiName, groupName, srcName);
        k8sSVC.createK8sFromYaml(groupName);
        k8sIngress.createK8sFromYaml(apiName);
        return Result.succeed(true);
    }

    /**
     * @Deprecated 删除sql接口 已弃用！！！
     */
    @Override
    public Result delSqlApiService(String apiName) throws IOException, ApiException {
        k8sDeployment.deleteK8sDeployment(apiName);
        k8sSVC.deleteK8sService(apiName + "-svc");
        k8sIngress.deleteIngress(apiName + "-ingress");
        return Result.succeed(true);
    }

    /**
     * @Deprecated  获取ingress列表 已弃用！！！
     */
    @Override
    public Result listSqlApi() throws IOException, ApiException {
        return k8sIngress.listIngress();
    }

    /**
     * 更新数据服务组副本数
     */
    @Override
    public Result updateReplicaNum(String groupName, int num) throws IOException, ApiException {
        AppsV1Api apiInstance = k8sCommon.getApiInstanceWithConfig();
        V1Deployment deployment = k8sDeployment.getK8sDeployment(groupName);
        String namespace = deployment.getMetadata().getNamespace();
        String deploymentName = deployment.getMetadata().getName();
        deployment.getSpec().setReplicas(num);
        apiInstance.replaceNamespacedDeployment(deploymentName, namespace, deployment, null, null, null);
        return Result.succeed(true).message("The number of replicas of data service " + groupName
            + "is updated to " + num);
    }

    /**
     * 设置自动调整数据服务组副本数范围
     */
    @Override
    public Result setAutoAdjustReplica(String groupName, int minNum, int maxNum) throws IOException {
        System.out.println("groupName: " + groupName + " minNum: " + minNum + " maxNum: " + maxNum);
        Map<String, Object> autoscalerMap = getV1HorizontalPodAutoscaler(groupName, minNum, maxNum);

        ApiClient apiClient = k8sCommon.getApiClientWithConfig();
        AutoscalingV1Api autoscalingApi = new AutoscalingV1Api(apiClient);
        try {
            autoscalingApi.createNamespacedHorizontalPodAutoscaler((String) autoscalerMap.get("namespace"),
                (V1HorizontalPodAutoscaler) autoscalerMap.get("autoscaler"), null, null, null);
            System.out.println("Autoscaler created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating autoscaler: " + e.getMessage());
        }
        return Result.succeed(true).message("Successfully enabled automatic expansion and contraction"
            + " of data services");
    }

    /**
     * 更新动态水平扩展范围
     */
    @Override
    public Result updateAutoAdjustReplicaNum(String groupName, int minNum, int maxNum) throws IOException {
        Map<String, Object> autoscalerMap = getV1HorizontalPodAutoscaler(groupName, minNum, maxNum);

        ApiClient apiClient = k8sCommon.getApiClientWithConfig();
        AutoscalingV1Api autoscalingApi = new AutoscalingV1Api(apiClient);
        try {
            autoscalingApi.replaceNamespacedHorizontalPodAutoscaler(autoscalerMap.get("deploymentName") + "-autoscaler",
                (String) autoscalerMap.get("namespace"), (V1HorizontalPodAutoscaler) autoscalerMap.get("autoscaler"),
                null, null, null);
            System.out.println("Autoscaler updated successfully.");
        } catch (Exception e) {
            System.err.println("Error updating autoscaler: " + e.getMessage());
        }
        return Result.succeed(true).message("Successfully update automatic expansion and contraction of data services");
    }

    /**
     * 删除动态水平扩展
     */
    @Override
    public Result deleteAutoAdjustReplica(String groupName) throws IOException {
        V1Deployment deployment = k8sDeployment.getK8sDeployment(groupName);
        String namespace = Objects.requireNonNull(deployment.getMetadata()).getNamespace();
        String deploymentName = deployment.getMetadata().getName();
        String autoScaleName = deploymentName + "-autoscaler";

        ApiClient apiClient = k8sCommon.getApiClientWithConfig();
        AutoscalingV1Api autoscalingApi = new AutoscalingV1Api(apiClient);
        try {
            autoscalingApi.deleteNamespacedHorizontalPodAutoscaler(autoScaleName, namespace, null, null,
                null, null, null, null);
            System.out.println("Autoscaler deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting autoscaler: " + e.getMessage());
        }
        return Result.succeed(true).message("Successfully delete automatic expansion and contraction of data services");
    }

    /**
     * 获取动态水平扩展信息
     */
    private Map<String, Object> getV1HorizontalPodAutoscaler(String groupName, int minNum, int maxNum) {
        V1Deployment deployment = k8sDeployment.getK8sDeployment(groupName);
        String namespace = Objects.requireNonNull(deployment.getMetadata()).getNamespace();
        String deploymentName = deployment.getMetadata().getName();
        // define automatic scaling configuration
        V1HorizontalPodAutoscaler autoscaler = new V1HorizontalPodAutoscaler();
        autoscaler.setMetadata(new V1ObjectMeta().name(deploymentName + "-autoscaler").namespace(namespace));
        V1HorizontalPodAutoscalerSpec autoscalerSpec = new V1HorizontalPodAutoscalerSpec();
        autoscalerSpec.setTargetCPUUtilizationPercentage(DEFAULT_CPU_PER);
        autoscalerSpec.setMaxReplicas(maxNum);
        autoscalerSpec.setMinReplicas(minNum);
        autoscalerSpec.setScaleTargetRef(new V1CrossVersionObjectReference().kind("Deployment").name(deploymentName)
            .apiVersion("apps/v1"));
        autoscaler.setSpec(autoscalerSpec);

        Map<String, Object> map = new HashMap<>();
        map.put("deploymentName", deploymentName);
        map.put("namespace", namespace);
        map.put("autoscaler", autoscaler);
        return map;
    }

    /**
     * 创建configmap
     */
    @Override
    public Result createConfigMap(DataSourceInfo dataSourceInfo) throws IOException, ApiException {
        ApiClient apiClient = k8sCommon.getApiClientWithConfig();
        Configuration.setDefaultApiClient(apiClient);
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);

        String configMapName = dataSourceInfo.getDsName() + "-cm";
        String secretName = dataSourceInfo.getDsName() + "-secret";
        String namespace = "default";

        if (!dataSourceInfo.getSrcType().equals("mysql") && !dataSourceInfo.getSrcType().equals("mongodb")) {
            return Result.failed().message("不支持的数据源类型");
        }

        // 检查是否存在同名的 ConfigMap
        boolean configMapExists = false;
        try {
            coreV1Api.readNamespacedConfigMap(configMapName, namespace, null);
            configMapExists = true;
        } catch (ApiException e) {
            if (e.getCode() != NOT_FOUND) {
                return Result.failed(false).message("Exception when checking ConfigMap: " + e.getMessage());
            }
        }
        if (configMapExists) {
            return Result.failed().message("已存在同名数据源");
        }

        boolean secretExists = false;
        try {
            coreV1Api.readNamespacedSecret(secretName, namespace, null);
            secretExists = true;
        } catch (ApiException e) {
            if (e.getCode() != NOT_FOUND) {
                return Result.failed(false).message("Exception when checking Secret: " + e.getMessage());
            }
        }
        if (secretExists) {
            return Result.failed().message("已存在同名数据源");
        }

        V1ConfigMap configMap = new V1ConfigMap();
        configMap.setApiVersion("v1");
        configMap.setKind("ConfigMap");
        configMap.setMetadata(new V1ObjectMeta().name(configMapName));
        Map<String, String> dbInfo = new HashMap<>();
        dbInfo.put("HOST", dataSourceInfo.getHost());
        dbInfo.put("PORT", dataSourceInfo.getPort());
        dbInfo.put("DATABASE", dataSourceInfo.getDb());
        dbInfo.put("SRCTYPE", dataSourceInfo.getSrcType());
        configMap.setData(dbInfo);
        V1ConfigMap createdConfigMap = coreV1Api.createNamespacedConfigMap(namespace, configMap, null, null, null);

        V1Secret secret = new V1Secret();
        secret.setApiVersion("v1");
        secret.setKind("Secret");
        secret.setType("Opaque");
        secret.setMetadata(new V1ObjectMeta().name(secretName));
        Map<String, byte[]> dbSecret = new HashMap<>();
        dbSecret.put("USER", dataSourceInfo.getUser().getBytes());
        dbSecret.put("PASSWORD", dataSourceInfo.getPwd().getBytes());
        secret.setData(dbSecret);
        V1Secret createdSecret = coreV1Api.createNamespacedSecret(namespace, secret, null, null, null);

        return Result.succeed(true)
            .message("ConfigMap created: " + createdConfigMap.getMetadata().getName() + ", secret created: "
                + createdSecret.getMetadata().getName());
    }

    /**
     * 删除configmap
     */
    public Result deleteConfigMap(String srcName) throws IOException, ApiException {
        ApiClient apiClient = k8sCommon.getApiClientWithConfig();
        Configuration.setDefaultApiClient(apiClient);
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);

        String configMapName = srcName + "-cm";
        String secretName = srcName + "-secret";
        String namespace = "default";

        // 检查是否存在同名的 ConfigMap
        boolean configMapExists = false;
        try {
            coreV1Api.readNamespacedConfigMap(configMapName, namespace, null);
            configMapExists = true;
        } catch (ApiException e) {
            if (e.getCode() != NOT_FOUND) {
                return Result.failed(false).message("Exception when checking ConfigMap: " + e.getMessage());
            }
        }
        if (configMapExists) {
            coreV1Api.deleteNamespacedConfigMap(configMapName, namespace, null, null, null, null, null, null);
            System.out.println("Existing ConfigMap deleted: " + configMapName);
        }

        // 检查是否存在同名的 Secret
        boolean secretExists = false;
        try {
            coreV1Api.readNamespacedSecret(secretName, namespace, null);
            secretExists = true;
        } catch (ApiException e) {
            if (e.getCode() != NOT_FOUND) {
                return Result.failed(false).message("Exception when checking Secret: " + e.getMessage());
            }
        }
        if (secretExists) {
            coreV1Api.deleteNamespacedSecret(secretName, namespace, null, null, null, null, null, null);
            System.out.println("Existing Secret deleted: " + secretName);
        }

        return Result.succeed(true).message("成功删除" + srcName + "的configmap");

    }

    /**
     * 更新configmap
     */
    @Override
    public Result updateConfigMap(DataSourceInfo dataSourceInfo) throws IOException, ApiException {
        ApiClient apiClient = k8sCommon.getApiClientWithConfig();
        Configuration.setDefaultApiClient(apiClient);
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);

        String configMapName = dataSourceInfo.getDsName() + "-cm";
        String secretName = dataSourceInfo.getDsName() + "-secret";
        String namespace = "default";

        // 读取现有的 ConfigMap
        V1ConfigMap existingConfigMap = coreV1Api.readNamespacedConfigMap(configMapName, namespace, null);
        // 配置新的data
        Map<String, String> dbInfo = new HashMap<>();
        dbInfo.put("HOST", dataSourceInfo.getHost());
        dbInfo.put("PORT", dataSourceInfo.getPort());
        dbInfo.put("DATABASE", dataSourceInfo.getDb());
        existingConfigMap.setData(dbInfo);
        V1ConfigMap updatedConfigMap = coreV1Api.replaceNamespacedConfigMap(configMapName, namespace, existingConfigMap,
            null, null, null);

        // 读取现有的 Secret 对象
        V1Secret existingSecret = coreV1Api.readNamespacedSecret(secretName, namespace, null);
        Map<String, byte[]> mysqlSecret = new HashMap<>();
        mysqlSecret.put("USER", dataSourceInfo.getUser().getBytes());
        mysqlSecret.put("PASSWORD", dataSourceInfo.getPwd().getBytes());
        existingSecret.setData(mysqlSecret);
        V1Secret updatedSecret = coreV1Api.replaceNamespacedSecret(secretName, namespace, existingSecret,
            null, null, null);

        return Result.succeed(true)
                .message("ConfigMap updated: " + updatedConfigMap.getMetadata().getName() + ", secret updated: "
                    + updatedSecret.getMetadata().getName());
    }

    /**
     * 创建deployment
     */
    @Override
    public Result createDeployment(CreateDeploymentInfo createDeploymentInfo) throws IOException, ApiException {
        k8sSVC.createK8sFromYaml(createDeploymentInfo.getGroupName());
        k8sDeployment.createDeploymentFromYamlInSvc(createDeploymentInfo.getSrcName(),
            createDeploymentInfo.getGroupName());
        return Result.succeed(true);
    }

    /**
     * 删除Deployment
     */
    @Override
    public Result deleteDeployment(String name) throws IOException, ApiException {
        k8sSVC.deleteK8sService(name + "-svc");
        k8sDeployment.deleteK8sDeployment(name);
        return Result.succeed(true);
    }

    /**
     * 创建ingress
     */
    @Override
    public String createIngress(CreateIngressInfo createIngressInfo) throws IOException, ApiException {
        k8sIngress.createK8sFromYaml(createIngressInfo.getGroupName());
        return "success";
    }

    /**
     * 删除Ingress
     */
    @Override
    public String deleteIngress(String groupName) throws IOException, ApiException {
        k8sIngress.deleteIngress(groupName + "-ingress");
        ApiClient apiClient = k8sCommon.getApiClientWithConfig();
        Configuration.setDefaultApiClient(apiClient);
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        boolean svcExists = false;
        try {
            coreV1Api.readNamespacedService(groupName + "-svc", "default", null);
            svcExists = true;
        } catch (ApiException e) {
            if (e.getCode() != NOT_FOUND) {
                return "Exception when checking Service: " + e.getMessage();
            }
        }
        if (svcExists) {
            k8sSVC.deleteK8sService(groupName + "-svc");
            System.out.println("Existing svc deleted: " + groupName + "-svc");
        }
        try {
            V1DeploymentList deploymentList = appsV1Api.listNamespacedDeployment("default", null, null,
                null, null, null, null, null, null, null, null);
            for (V1Deployment deployment : deploymentList.getItems()) {
                if (groupName.equals(deployment.getMetadata().getName())) {
                    k8sDeployment.deleteK8sDeployment(groupName);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception when checking deployment");
        }
        return "success";
    }
}
