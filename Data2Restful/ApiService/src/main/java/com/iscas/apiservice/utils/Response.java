package com.iscas.apiservice.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Schema
@NoArgsConstructor
public final class Response {
    private static final int SUCCESS_CODE = 20000;
    private static final int ERROR_CODE = 200001;
    @Schema(example = "true", description = "是否成功")
    private boolean success;
    @Schema(example = "20000", description = "内部状态码")
    private int code;
    @Schema(example = "成功", description = "状态信息")
    private String message;
    @Schema(description = "返回实体，类型取决于具体的接口")
    private Map<String, Object> data = new HashMap<>();

    public static Response ok() {
        Response r = new Response();
        r.setSuccess(true);
        r.setCode(SUCCESS_CODE);
        r.setMessage("成功");
        return r;
    }

    public static Response error() {
        Response r = new Response();
        r.setSuccess(false);
        r.setCode(ERROR_CODE);
        r.setMessage("失败");
        return r;
    }

    public Response success(Boolean isSuccess) {
        this.setSuccess(isSuccess);
        return this;
    }

    public Response message(String inMessage) {
        this.setMessage(inMessage);
        return this;
    }

    public Response code(int inCode) {
        this.setCode(inCode);
        return this;
    }

    public Response data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Response data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

}
