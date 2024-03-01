package com.iscas.k8scli.services.algoService;

import com.iscas.k8scli.pojo.AppInfo;
import com.iscas.k8scli.response.Result;

public interface AlgoService {

    Result publicApi(String name, String group, String code);

    Result publicAlgo(AppInfo appInfo);


    Result publicAlgo(String type, String code);

    AppInfo getAlgo(String name);
}
