package com.example.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gateway.mapper.ApiMapper;
import com.example.gateway.pojo.Api;
import com.example.gateway.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/*
 *@title ApiServiceImpl
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/13 10:07
 */
@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private ApiMapper apiMapper;

    /**
     * 判断数据服务接口是否公开.
     */
    @Override
    public boolean getApiPublicOrNot(String name) {
        Api api = apiMapper.selectOne(new LambdaQueryWrapper<Api>().eq(Api::getName, name));
        if (Objects.isNull(api)) {
            throw new RuntimeException("不存在名为" + name + "的api");
        }
        return api.getIsPublic() == 1;
    }
}
