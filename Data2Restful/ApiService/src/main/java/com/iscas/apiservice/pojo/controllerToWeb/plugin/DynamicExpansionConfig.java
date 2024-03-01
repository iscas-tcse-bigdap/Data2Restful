package com.iscas.apiservice.pojo.controllerToWeb.plugin;

import com.iscas.apiservice.pojo.PluginDynamicExpansion;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wbq
 * @version 1.0
 * @title DynamicExpansionConfig
 * @description
 * @create 2023/11/15 10:40
 */


@Data
@NoArgsConstructor
public class DynamicExpansionConfig {
    private int minValue;
    private int maxValue;

    public DynamicExpansionConfig(int min, int max) {
        this.minValue = min;
        this.maxValue = max;
    }

    public DynamicExpansionConfig(PluginDynamicExpansion pluginDynamicExpansion) {
        this.minValue = pluginDynamicExpansion.getMinValue();
        this.maxValue = pluginDynamicExpansion.getMaxValue();
    }
}
