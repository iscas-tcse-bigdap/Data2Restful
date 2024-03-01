package com.iscas.apiservice.pojo.controllerToWeb.plugin;

import com.iscas.apiservice.pojo.PluginDynamicRateLimit;
import lombok.Data;

/**
 * @author wbq
 * @version 1.0
 * @title RateLimitConfig
 * @description
 * @create 2023/11/9 15:39
 */

@Data
public class RateLimitConfig {
    private int rateLimit;
    private int timeWindow;

    public RateLimitConfig(PluginDynamicRateLimit newRateLimit) {
        this.rateLimit = newRateLimit.getRateLimit();
        this.timeWindow = newRateLimit.getTimeWindow();
    }
}
