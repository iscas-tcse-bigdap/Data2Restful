package com.iscas.apiservice.pojo.controllerToWeb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.apiservice.pojo.PluginAcl;
import com.iscas.apiservice.pojo.PluginDynamicExpansion;
import com.iscas.apiservice.pojo.PluginDynamicRateLimit;
import com.iscas.apiservice.pojo.PluginKeyAuth;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.AclConfig;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.DynamicExpansionConfig;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.KeyAuthConfig;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.RateLimitConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
 *@title PluginInfo
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/25 13:55
 */
@Data
@NoArgsConstructor
public class PluginDetail {
    private int pluginId;
    private int groupId;
    private boolean status;
    private String name;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Object pluginConfig;

    public PluginDetail(PluginKeyAuth pluginKeyAuth, KeyAuthConfig keyAuthConfig) {
        this.pluginId = pluginKeyAuth.getKeyAuthId();
        this.groupId = pluginKeyAuth.getGroupId();
        this.status = pluginKeyAuth.getStatus() == 1;
        this.name = "key-auth";
        this.updateTime = pluginKeyAuth.getUpdateTime();
        this.pluginConfig = keyAuthConfig;
    }

    public PluginDetail(PluginAcl pluginAcl, AclConfig aclConfig) {
        this.pluginId = pluginAcl.getAclId();
        this.groupId = pluginAcl.getGroupId();
        this.status = pluginAcl.getStatus() == 1;
        this.name = "acl";
        this.updateTime = pluginAcl.getUpdateTime();
        this.pluginConfig = aclConfig;
    }

    public PluginDetail(PluginDynamicRateLimit pluginDynamicRateLimit, RateLimitConfig rateLimitConfig) {
        this.pluginId = pluginDynamicRateLimit.getRateLimitId();
        this.groupId = pluginDynamicRateLimit.getGroupId();
        this.status = pluginDynamicRateLimit.getStatus() == 1;
        this.name = "rate-limit";
        this.updateTime = pluginDynamicRateLimit.getUpdateTime();
        this.pluginConfig = rateLimitConfig;
    }

    public PluginDetail(PluginDynamicExpansion pluginDynamicExpansion, DynamicExpansionConfig dynamicExpansionConfig) {
        this.pluginId = pluginDynamicExpansion.getDynamicExpansionId();
        this.groupId = pluginDynamicExpansion.getGroupId();
        this.status = pluginDynamicExpansion.getStatus() == 1;
        this.name = "dynamic-expansion";
        this.updateTime = pluginDynamicExpansion.getUpdateTime();
        this.pluginConfig = dynamicExpansionConfig;
    }
}
