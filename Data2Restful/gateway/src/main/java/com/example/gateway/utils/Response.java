package com.example.gateway.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public final class Response {
    private boolean success;

    private int code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    public Response() { }

    public static final int SUCCESS = 20000;

    public static final int ERROR = 20001;

    public static final int PASS = 20200;   // 放行状态码

    public static final int UNAUTHORIZED = 40001;

    public static final int FORBIDDEN = 40003;

    public static final int TOOMANYREQUEST = 40029;

    public static Response ok() {
        Response r = new Response();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static Response error() {
        Response r = new Response();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public static Response pass() {
        Response r = new Response();
        r.setSuccess(true);
        r.setCode(ResultCode.PASS);
        r.setMessage("放行");
        return r;
    }

    public static Response unauthorized() {
        Response r = new Response();
        r.setSuccess(false);
        r.setCode(ResultCode.UNAUTHORIZED);
        r.setMessage("未认证");
        return r;
    }

    public static Response forbidden() {
        Response r = new Response();
        r.setSuccess(false);
        r.setCode(ResultCode.FORBIDDEN);
        r.setMessage("权限不足");
        return r;
    }


    public static Response toManyRequests() {
        Response r = new Response();
        r.setSuccess(false);
        r.setCode(ResultCode.TOOMANYREQUEST);
        r.setMessage("请求过于频繁，请稍后再试");
        return r;
    }

    public Response success(Boolean newSuccess) {
        this.setSuccess(newSuccess);
        return this;
    }

    public Response message(String newMessage) {
        this.setMessage(newMessage);
        return this;
    }

    public Response code(int newCode) {
        this.setCode(newCode);
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean newSuccess) {
        this.success = newSuccess;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int newCode) {
        this.code = newCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object getDataObject(String key) {
        return data.get(key);
    }

    public void setData(Map<String, Object> newData) {
        this.data = newData;
    }


    public boolean isOk() {
        return code == ResultCode.SUCCESS;
    }

    public boolean isError() {
        return code == ResultCode.ERROR;
    }

    public boolean isForbidden() {
        return code == ResultCode.FORBIDDEN;
    }

    public boolean isPass() {
        return code == ResultCode.PASS;
    }

    public boolean isUnauthorized() {
        return code == ResultCode.UNAUTHORIZED;
    }
}
