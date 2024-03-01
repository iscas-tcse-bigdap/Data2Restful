package com.iscas.k8scli.services.kubernetes.pods;

import com.iscas.k8scli.services.kubernetes.common.K8sCommon;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

/**
 * @ClassName: K8sPods
 * @Description:
 * @Author: wzc
 * @Date: 2023/8/6 23:22
 */
@Service
public class K8sPods {

    @Autowired
    private K8sCommon k8sCommon;


    /***
     * @Author wzc
     * @Description 查看Pods清单
     * @Date 23:24 2023/8/6
     * @Param [name]
     * @return io.kubernetes.client.openapi.models.V1PodList
     **/
    public V1PodList listK8sPod(String name) throws ApiException, IOException {
        CoreV1Api apiCore = k8sCommon.getCoreApiInstanceWithConfig();
        V1PodList list = null;
        String namespace = k8sCommon.getNamespace();
        list = apiCore.listNamespacedPod(namespace, "true", null, null, null, "app=" + name,
            null, null, null, null,
            null);

        return list;
    }

    /***
     * @Author wzc
     * @Description 获取pods
     * @Date 23:24 2023/8/6
     * @Param []
     * @return io.kubernetes.client.openapi.models.V1PodList
     **/
    public V1PodList getK8sPods() throws ApiException, IOException {
        V1PodList result = null;
        String namespace = k8sCommon.getNamespace();
        CoreV1Api apiCore = k8sCommon.getCoreApiInstanceWithConfig();
        result = apiCore.listNamespacedPod(namespace, "true", null, null, null, null,
            null, null, null, null, null);

        return result;
    }

    public static void main(String[] args) throws IOException, ApiException {
//        InputStream config_path = K8sPods.class.getClassLoader().getResourceAsStream("kubernetesconfig_proc");

        // 读取 kubeconfig 文件的内容
        KubeConfig kubeConfig = KubeConfig.loadKubeConfig(new FileReader("D:\\软件所\\数据中台\\项目\\dataCenter\\dataCenter"
            + "\\k8scli\\src\\main\\resources\\k8sconfigs\\kubernetesconfig_test.yaml"));

        //加载k8s,confg
        ApiClient client = ClientBuilder.kubeconfig(kubeConfig).build();
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        CoreV1Api apiCore = new CoreV1Api(client);

        V1PodList list = null;
        list = apiCore.listNamespacedPod("default", "true", null, null, null,
            null, null, null, null, null, null);
        System.out.println(list);
    }
}
