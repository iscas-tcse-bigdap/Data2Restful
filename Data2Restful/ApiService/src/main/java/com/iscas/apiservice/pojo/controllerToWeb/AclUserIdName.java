package com.iscas.apiservice.pojo.controllerToWeb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wbq
 * @version 1.0
 * @title AclUserIdName
 * @description
 * @create 2023/11/14 11:21
 */

@Data
public class AclUserIdName {
    @Schema(example = "1", description = "用户ID")
    private int userId;

    @Schema(example = "wbq", description = "用户名")
    private String value;
}
