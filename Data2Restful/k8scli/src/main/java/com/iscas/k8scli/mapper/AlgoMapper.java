package com.iscas.k8scli.mapper;

import com.iscas.k8scli.pojo.AppInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: AlgoMapper
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/25 11:21
 */
@Mapper
public interface AlgoMapper {

    int addAlgo(AppInfo appInfo);

    AppInfo getAlog(String name);
}
