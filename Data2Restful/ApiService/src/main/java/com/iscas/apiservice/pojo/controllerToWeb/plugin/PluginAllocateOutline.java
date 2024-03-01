package com.iscas.apiservice.pojo.controllerToWeb.plugin;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wbq
 * @version 1.0
 * @title PluginAllocateOutline
 * @description
 * @create 2023/10/29 14:24
 */
@Data
@NoArgsConstructor
public class PluginAllocateOutline {
    private String name;
    private String desc;

    public PluginAllocateOutline(String newName, String newDesc) {
        this.name = newName;
        this.desc = newDesc;
    }
}
