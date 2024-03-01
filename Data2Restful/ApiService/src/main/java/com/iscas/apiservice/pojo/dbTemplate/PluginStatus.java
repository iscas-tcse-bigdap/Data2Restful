package com.iscas.apiservice.pojo.dbTemplate;

import lombok.Data;

/**
 *@title pluginStatus
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/25 13:50
 */
@Data
public class PluginStatus {
    private int keyAuthStatus;
    private int aclStatus;
    private int rateLimitStatus;
    private int dynamicExpansionStatus;
}
