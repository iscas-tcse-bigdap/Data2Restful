package com.iscas.apiservice.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wbq
 * @version 1.0
 * @title ErrorModel
 * @description
 * @create 2024/1/2 15:47
 */

@Schema(description = "错误信息")
@Setter
@Getter
public class ErrorModel {
    @Schema(example = "false", description = "是否成功")
    private boolean success;

    @Schema(example = "500", description = "状态码")
    private int code;

    @Schema(example = "error message", description = "状态信息")
    private String message;

    @Schema(example = "null")
    private Map<String, Object> data = new HashMap<>();
}
