package com.iscas.k8scli.mapper;

import com.iscas.k8scli.pojo.AppInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppMapper {
    AppInfo getAppInfo(String name);

    List<AppInfo> listApps();
}
