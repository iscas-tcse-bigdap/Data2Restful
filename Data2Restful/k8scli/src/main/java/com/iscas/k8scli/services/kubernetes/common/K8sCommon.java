package com.iscas.k8scli.services.kubernetes.common;

import com.iscas.k8scli.services.kubernetes.impl.K8sServiceImpl;
import io.kubernetes.client.openapi.apis.ApiextensionsV1Api;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: k8sCommon
 * @Description:
 * @Author: wzc
 * @Date: 2023/7/26 9:26
 */
@Component
public class K8sCommon {

    /**
     * -- GETTER --
     *  获取命名空间 namespace
     *
     * @return
     */
    @Getter
    @Value("${zc.k8s.namespace:default}")
    private String namespace;

    @Value("${zc.k8s.config.file}")
    private String k8sConfig;


    /**
     * 根据配置获取api实例
     */
    public AppsV1Api getApiInstanceWithConfig() throws IOException {
        InputStream configPath = K8sServiceImpl.class.getClassLoader().getResourceAsStream(k8sConfig);
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        return new AppsV1Api(client);
    }

    /**
     * 根据配置获取api客户端
     */
    public ApiClient getApiClientWithConfig() throws IOException {
        InputStream configPath = K8sServiceImpl.class.getClassLoader().getResourceAsStream(k8sConfig);
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        return client;
    }

    /**
     * 根据配置获取coreAPI实例
     */
    public CoreV1Api getCoreApiInstanceWithConfig() throws IOException {
        InputStream configPath = K8sServiceImpl.class.getClassLoader().getResourceAsStream(k8sConfig);
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        return new CoreV1Api(client);
    }

    /**
     * 根据配置获取api扩展
     */
    public ApiextensionsV1Api getApiextensionsV1ApiWithConfig() throws IOException {
        InputStream configPath = K8sServiceImpl.class.getClassLoader().getResourceAsStream(k8sConfig);
        //加载k8s,confg
        ApiClient client = Config.fromConfig(configPath);
        client.setReadTimeout(0); //设置为0 永不超时
        Configuration.setDefaultApiClient(client);
        return new ApiextensionsV1Api(client);
    }

}
