package com.example.gateway.service.impl;

import com.example.gateway.mapper.RateLimitMapper;
import com.example.gateway.pojo.RateLimit;
import com.example.gateway.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title RateLimitServiceImpl
 * @description
 * @create 2023/11/2 11:07
 */

@Service
public class RateLimitServiceImpl implements RateLimitService {

    @Autowired
    private RateLimitMapper rateLimitMapper;

    /**
     * 获取流控配置信息.
     */
    @Override
    public List<RateLimit> getRateLimit() {
        return rateLimitMapper.getRateLimitInfo();
    }
}
