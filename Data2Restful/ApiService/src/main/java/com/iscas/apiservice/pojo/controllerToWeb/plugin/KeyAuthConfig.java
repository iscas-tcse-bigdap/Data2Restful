package com.iscas.apiservice.pojo.controllerToWeb.plugin;

import com.iscas.apiservice.pojo.PluginKeyAuth;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *@title KeyAuthConfig
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/25 14:01
 */
@Data
@NoArgsConstructor
public class KeyAuthConfig {
    private String keyName;
    private String keyPwd;

    public KeyAuthConfig(String name, String pwd) {
        this.keyName = name;
        this.keyPwd = pwd;
    }

    public KeyAuthConfig(PluginKeyAuth pluginKeyAuth) {
        this.keyName = pluginKeyAuth.getKeyName();
        this.keyPwd = pluginKeyAuth.getKeyPwd();
    }
}
