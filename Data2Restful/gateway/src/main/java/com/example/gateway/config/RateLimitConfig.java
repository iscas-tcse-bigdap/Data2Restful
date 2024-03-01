package com.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.example.gateway.handler.SentinelFallbackHandler;
import com.example.gateway.pojo.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wbq
 * @version 1.0
 * @title RateLimitConfig
 * @description
 * @create 2023/11/1 16:38
 */
@Slf4j
@Configuration
public class RateLimitConfig {

    @Lazy
    @Autowired
    private RefreshableRateLimitRules refreshableRateLimitRules;

    @Lazy
    @Autowired
    private RefreshableRateLimitGroup refreshableRateLimitGroup;

    /**
     * sentinel异常处理.
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler() {
        return new SentinelFallbackHandler();
    }

    /**
     * sentinel流控规则配置.
     */
    @PostConstruct
    public void doInit() {
        log.info("正在进行流控规则配置 -> -------------------");
        GatewayApiDefinitionManager.register2Property(refreshableRateLimitGroup.getProperty());
        GatewayRuleManager.register2Property(refreshableRateLimitRules.getProperty());
    }

    /**
     *  网关限流规则
     */
    private void initGatewayRules(List<RateLimit> rateLimits) {
        log.info("正在进行流控规则配置 -> 当前流控规则为:{}", Arrays.toString(rateLimits.toArray()));
        Set<GatewayFlowRule> rules = new HashSet<>();
        for (RateLimit rateLimit : rateLimits) {
            log.info("配置访问频率限制 -> 当前配置的分组是{}，限流阈值是{}，时间窗口是{}秒，限制路径是：\"/svc/\"{}\"/**\"",
                    rateLimit.getName(), rateLimit.getRateLimit(), rateLimit.getTimeWindow(), rateLimit.getName());
            rules.add(new GatewayFlowRule("limitGroup-" + rateLimit.getName())
                    .setCount(rateLimit.getRateLimit())
                    .setIntervalSec(rateLimit.getTimeWindow())
                    .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME));
        }
        GatewayRuleManager.loadRules(rules);
    }

    /**
     *  初始化 限流api 分组信息
     */

    private static void initCustomizedApis(List<RateLimit> rateLimits) {
        Set<ApiDefinition> definitions = new HashSet<>();
        for (RateLimit rateLimit : rateLimits) {
            ApiDefinition apiGroup = new ApiDefinition().setApiName("limitGroup-" + rateLimit.getName())
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                {
                    add(new ApiPathPredicateItem()
                            .setPattern("/svc/" + rateLimit.getName() + "/**").
                            setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }
            });
            definitions.add(apiGroup);
        }
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}
