package com.iscas.k8scli.services.kubernetes.ingress;

import com.iscas.k8scli.pojo.K8sIngress;
import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.kubernetes.common.K8sCommon;
import com.iscas.k8scli.utils.common.FileUtil;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;

import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.models.V1HTTPIngressPath;
import io.kubernetes.client.openapi.models.V1HTTPIngressRuleValue;
import io.kubernetes.client.openapi.models.V1Ingress;
import io.kubernetes.client.openapi.models.V1IngressBackend;
import io.kubernetes.client.openapi.models.V1IngressList;
import io.kubernetes.client.openapi.models.V1IngressRule;
import io.kubernetes.client.openapi.models.V1IngressServiceBackend;
import io.kubernetes.client.openapi.models.V1IngressSpec;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1ServiceBackendPort;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: K8sIngress
 * @Description:
 * @Author: wzc
 * @Date: 2023/8/6 23:43
 */
@Service
public class K8sIngressService {

    @Autowired
    private K8sCommon k8sCommon;

    private String pattern;

    private ApiClient apiClient;

    /**
     * ingress配置文件选择
     */
    @PostConstruct
    public void postConstruct() throws IOException {
        pattern = FileUtil.getResourceTemplate("/k8sYamlTemplate/ingress/base_ingress.yaml");
        apiClient = k8sCommon.getApiClientWithConfig();
        Configuration.setDefaultApiClient(apiClient);
    }

    /**
     * @Deprecated 该接口只用于测试ingress创建
     */
    public Result testCreate() throws IOException, ApiException {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "test");
        createIngress("test", "default", "app-test-svc", "5080", "/a(/|$)(.*)", "Prefix", labels);
        return null;
    }

    /**
     * 创建ingress
     */
    public Result createIngress(String name, String namespace, String svcName, String backendPort, String path,
                                String pathType, Map<String, String> labels) throws IOException, ApiException {
        NetworkingV1Api api = new NetworkingV1Api();

        V1IngressServiceBackend v1IngressServiceBackend = new V1IngressServiceBackend().name(svcName)
            .port(new V1ServiceBackendPort().number(Integer.valueOf(backendPort)));
        // 创建 IngressRuleValue
        V1HTTPIngressPath ingressPath = new V1HTTPIngressPath()
                .path(path)
                .pathType(pathType)
                .backend(new V1IngressBackend()
                        .service(v1IngressServiceBackend)
                );

        V1HTTPIngressRuleValue ingressRuleValue = new V1HTTPIngressRuleValue()
                .paths(Collections.singletonList(ingressPath));

        // 创建 IngressRule
        V1IngressRule ingressRule = new V1IngressRule()
                .http(ingressRuleValue);

        // 创建 IngressSpec
        V1IngressSpec ingressSpec = new V1IngressSpec()
                .rules(Collections.singletonList(ingressRule));

        HashMap hashMap = new HashMap<>();
        hashMap.put("kubernetes.io/ingress.class", "nginx");
        hashMap.put("nginx.ingress.kubernetes.io/rewrite-target", "/$2");

        // 创建 Ingress
        V1Ingress ingress = new V1Ingress()
                .apiVersion("networking.k8s.io/v1")
                .kind("Ingress")
                .metadata(new V1ObjectMeta().name(name + "ingress").annotations(hashMap).labels(labels))
                .spec(ingressSpec);

        System.out.println(Yaml.dump(ingress));

        // 提交 Ingress 资源到 Kubernetes 集群
        api.createNamespacedIngress(namespace, ingress, null, null, null);
        System.out.println("Ingress created successfully.");
        return null;
    }

    /***
     * @Author wzc
     * @Description 通过yaml文件创建svc
     * @Date 23:36 2023/8/11
     * @Param [apiName]
     * @return com.iscas.k8scli.response.Result
     **/
    public Result createK8sFromYaml(String groupName) throws IOException, ApiException {

        NetworkingV1Api api = new NetworkingV1Api();

        String template = pattern.replace("${groupName}", groupName);
        V1Ingress ing = Yaml.loadAs(template, V1Ingress.class);
        V1Ingress createResult = api.createNamespacedIngress("default", ing, null, null, null);
        return Result.succeed("succeed");
    }

    /**
     * 获取ingress列表
     */
    public Result listIngress() throws IOException, ApiException {
        NetworkingV1Api api = new NetworkingV1Api();
        List<K8sIngress> result = new ArrayList<>();
        // 获取所有 Namespace 中的 Ingress 资源清单
        V1IngressList ingressList = api.listNamespacedIngress("default",  null, null, null, null,
            "app=data-center-api", null, null, null, null, null);
        // 输出 Ingress 资源信息
        for (V1Ingress ingress : ingressList.getItems()) {
            K8sIngress ingress1 = new K8sIngress();
            ingress1.setNamespace(ingress.getMetadata().getNamespace());
            ingress1.setName(ingress.getMetadata().getName());
            ingress1.setLabels(ingress.getMetadata().getLabels().toString());
            result.add(ingress1);
        }
        return Result.succeed(result);
    }

    /**
     * 删除ingress
     */
    public Result deleteIngress(String ingressName) throws IOException, ApiException {
        NetworkingV1Api api = new NetworkingV1Api();
        // 获取所有 Namespace 中的 Ingress 资源清单
        V1Status aDefault = api.deleteNamespacedIngress(ingressName, "default", null, null, null, null,
                null, null);
        System.out.println(aDefault);
        return Result.success();
    }

    public static void main(String[] args) throws IOException {

    }
}
