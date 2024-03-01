package com.iscas.k8scli.services.app;

import com.iscas.k8scli.pojo.AppInfo;

import java.util.List;

public interface AppService {

    AppInfo getAppInfo(String name);

    List<AppInfo> getAppInfos();
}
