package com.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.datasource.AutoRefreshDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.example.gateway.mapper.RateLimitMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wbq
 * @version 1.0
 * @title MySqlDataSource
 * @description
 * @create 2023/11/2 10:55
 */

@Slf4j
@Service
public class RefreshableRateLimitGroup extends AutoRefreshDataSource<List<String>, Set<ApiDefinition>> {
    private static final long DEFAULT_REFRESH_MS = 3000;

    @Autowired
    private RateLimitMapper rateLimitMapper;

    /**
     * 将groupName转化未sentinel识别的ApiDefinitionY以实现流控配置.
     */
    @Bean(name = "groupName2ApiDefinition")
    public Converter<List<String>, Set<ApiDefinition>> groupName2ApiDefinitionConverter() {
        return source -> source.stream().map(
            groupName -> new ApiDefinition().setApiName("limitGroup-" + groupName)
            .setPredicateItems(new HashSet<ApiPredicateItem>() {
            {
                add(new ApiPathPredicateItem()
                        .setPattern("/svc/" + groupName + "/**").
                        setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }
        })).collect(Collectors.toSet());
    }

    public RefreshableRateLimitGroup(@Lazy @Qualifier("groupName2ApiDefinition") Converter<List<String>,
        Set<ApiDefinition>> configParser) {
        super(configParser, DEFAULT_REFRESH_MS);
        firstLoad();
    }

    private void firstLoad() {
        try {
            Set<ApiDefinition> newValue = loadConfig();
            getProperty().updateValue(newValue);
        } catch (Throwable e) {
            RecordLog.info("loadConfig exception", e);
        }
    }

    /**
     * 获取开启流量控制的组名称.
     */
    @Override
    public List<String> readSource() {
        return rateLimitMapper.getRateLimitedSvc();
    }
}
