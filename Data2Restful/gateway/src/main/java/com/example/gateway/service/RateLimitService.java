package com.example.gateway.service;

import com.example.gateway.pojo.RateLimit;

import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title RateLimitService
 * @description
 * @create 2023/11/2 11:06
 */
public interface RateLimitService {
    List<RateLimit> getRateLimit();
}
