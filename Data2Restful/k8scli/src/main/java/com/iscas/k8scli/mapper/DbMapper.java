package com.iscas.k8scli.mapper;

import com.iscas.k8scli.pojo.AppInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DbMapper {
    AppInfo getAppInfo(String name);
}
