package com.iscas.apiservice.pojo.controllerToWeb;

import com.iscas.apiservice.pojo.PluginAclDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title Acl
 * @description 这一实体类主要用于gateway中的黑白名单获取，本服务中的接口均已使用其他更通用的实体类
 * @create 2023/11/8 10:05
 */

@Data
@NoArgsConstructor
public class Acl {
    @Schema(description = "白名单列表")
    private List<PluginAclDetail> whiteList;
    @Schema(description = "黑名单列表")
    private List<PluginAclDetail> blackList;

    public Acl(List<PluginAclDetail> newWhiteList, List<PluginAclDetail> newBlackList) {
        this.whiteList = newWhiteList;
        this.blackList = newBlackList;
    }
}
