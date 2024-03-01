package com.iscas.apiservice.pojo.controllerToWeb.plugin;

import com.iscas.apiservice.pojo.controllerToWeb.AclUserIdName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *@title AclConfig
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/25 14:02
 */
@Data
@NoArgsConstructor
public class AclConfig {
    private List<AclUserIdName> whitelist;
    private List<AclUserIdName> blacklist;

    public AclConfig(List<AclUserIdName> newWhitelist, List<AclUserIdName> newBlacklist) {
        this.whitelist = newWhitelist;
        this.blacklist = newBlacklist;
    }
}
