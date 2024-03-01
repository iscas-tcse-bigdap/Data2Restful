package com.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.datasource.AutoRefreshDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.example.gateway.mapper.RateLimitMapper;
import com.example.gateway.pojo.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wbq
 * @version 1.0
 * @title MysqlRefreshDataSource
 * @description
 * @create 2023/11/2 15:44
 */

@Service
public class RefreshableRateLimitRules extends AutoRefreshDataSource<List<RateLimit>, Set<GatewayFlowRule>> {
    private static final long DEFAULT_REFRESH_MS = 3000;

    @Autowired
    private RateLimitMapper rateLimitMapper;

    /**
     * 将RateLimit信息转化未sentinel识别的FlowRule以实现流控配置.
     */
    @Bean(name = "RateLimit2GatewayFlowRule")
    public Converter<List<RateLimit>, Set<GatewayFlowRule>> rateLimit2GatewayFlowRuleConverter() {
        return source -> source.stream().map(rateLimit -> new GatewayFlowRule("limitGroup-" + rateLimit.getName())
                .setCount(rateLimit.getRateLimit())
                .setIntervalSec(rateLimit.getTimeWindow())
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)).collect(Collectors.toSet());
    }

    public RefreshableRateLimitRules(@Lazy @Qualifier("RateLimit2GatewayFlowRule") Converter<List<RateLimit>,
        Set<GatewayFlowRule>> configParser) {
        super(configParser, DEFAULT_REFRESH_MS);
        firstLoad();
    }

    private void firstLoad() {
        try {
            Set<GatewayFlowRule> newValue = loadConfig();
            getProperty().updateValue(newValue);
        } catch (Throwable e) {
            RecordLog.info("loadConfig exception", e);
        }
    }

    /**
     * 从数据源中读取完整的rateLimit配置信息.
     */
    @Override
    public List<RateLimit> readSource() {
        return rateLimitMapper.getRateLimitInfo();
    }
}
