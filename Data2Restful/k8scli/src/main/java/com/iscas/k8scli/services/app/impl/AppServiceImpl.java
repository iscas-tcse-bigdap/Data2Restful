package com.iscas.k8scli.services.app.impl;

import com.iscas.k8scli.mapper.AppMapper;
import com.iscas.k8scli.pojo.AppInfo;
import com.iscas.k8scli.services.app.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: AppServiceImpl
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/25 10:26
 */
@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;

    /**
     * get app info
     */
    @Override
    public AppInfo getAppInfo(String name) {
        return appMapper.getAppInfo(name);
    }

    /**
     * get app info list
     */
    @Override
    public List<AppInfo> getAppInfos() {
        return appMapper.listApps();
    }
}
