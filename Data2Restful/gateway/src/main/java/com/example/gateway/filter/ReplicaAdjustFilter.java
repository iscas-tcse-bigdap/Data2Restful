package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author wbq
 * @version 1.0
 * @title ReplicaAdjustFilter
 * @description
 * @create 2023/11/6 11:10
 */

@Slf4j
public class ReplicaAdjustFilter implements GlobalFilter {
    /**
     * 副本调整过滤器.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }
}
