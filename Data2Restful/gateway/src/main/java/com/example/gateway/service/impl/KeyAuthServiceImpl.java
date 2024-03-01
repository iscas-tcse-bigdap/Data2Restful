package com.example.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gateway.mapper.KeyAuthMapper;
import com.example.gateway.pojo.KeyAuth;
import com.example.gateway.service.KeyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @title KeyAuthServiceIMPL
 * @description
 * @author wbq
 * @version 1.0
 * @create 2023/10/24 17:16
 */
@Service
public class KeyAuthServiceImpl implements KeyAuthService {
    @Autowired
    private KeyAuthMapper keyAuthMapper;

    /**
     * 获取数据服务组的KeyAuth配置.
     */
    @Override
    public KeyAuth getKeyAuth(int groupId) {
        KeyAuth keyAuth = keyAuthMapper.selectOne(new LambdaQueryWrapper<KeyAuth>().eq(KeyAuth::getGroupId, groupId));
        if (Objects.isNull(keyAuth)) {
            throw new RuntimeException("不存在id为" + groupId + "的服务");
        }
        return keyAuth;
    }
}
