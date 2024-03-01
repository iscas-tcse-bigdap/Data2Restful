package com.iscas.apiservice.pojo.webToController;

import com.iscas.apiservice.pojo.controllerToWeb.AclUserIdName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title AclWithId
 * @description
 * @create 2023/11/14 14:42
 */

@Data
public class AclWithId {
    @Schema(description = "ACL插件ID")
    private int aclId;
    private List<AclUserIdName> whiteList;
    private List<AclUserIdName> blackList;
}
