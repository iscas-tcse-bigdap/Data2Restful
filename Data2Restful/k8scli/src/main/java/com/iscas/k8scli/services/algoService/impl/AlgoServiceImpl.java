package com.iscas.k8scli.services.algoService.impl;

import com.iscas.k8scli.mapper.AlgoMapper;
import com.iscas.k8scli.pojo.AppInfo;
import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.algoService.AlgoService;
import com.iscas.k8scli.services.kubernetes.K8sService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName: AlgoServiceImpl
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/25 11:15
 */
@Service
public class AlgoServiceImpl implements AlgoService {

    @Autowired
    private AlgoMapper algoMapper;

    @Autowired
    private K8sService k8sService;

    private static final int PORT = 200;

    /**
     * 发布api，该接口暂未应用
     */
    @Override
    public Result publicApi(String name, String group, String code) {
        return null;
    }

    /**
     * 发布算法，未应用
     */
    @Override
    public Result publicAlgo(AppInfo appInfo) {
        int c = algoMapper.addAlgo(appInfo);
        Result stringResult = k8sService.runApp(appInfo.getName());
        return stringResult;
    }

    /**
     * 发布算法，未应用
     */
    @Override
    public Result publicAlgo(String type, String code) {
        AppInfo appInfo = new AppInfo();
        //TODO app名称、  deploy名称、 Service名称 生成机制
        appInfo.setName("sqlapp-" + UUID.randomUUID());
        appInfo.setCode(code);
        appInfo.setInitInstanceNum(1);
        appInfo.setImage("docker-hub.bdp.pro:5000/pyflask:v1.0.0");

        //TODO 端口分配机制
        appInfo.setContainerPort(PORT);
        appInfo.setServicePort(PORT);

        int c = algoMapper.addAlgo(appInfo);
        Result stringResult = k8sService.runApp(appInfo.getName());
        return stringResult;
    }

    /**
     * 获取算法信息
     */
    @Override
    public AppInfo getAlgo(String name) {
        return algoMapper.getAlog(name);
    }

    private AppInfo buildApp(String type, String code) {
        AppInfo appInfo = new AppInfo();

        return appInfo;
    }
}
