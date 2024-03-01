package cn.iscas.userauth.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class Response {
    private boolean success;

    private int code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    public Response() {
    }

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

    public static Response unauthorized() {
        Response r = new Response();
        r.setSuccess(false);
        r.setCode(ResultCode.UNAUTHORIZED);
        r.setMessage("认证失败");
        return r;
    }

    public static Response forbidden() {
        Response r = new Response();
        r.setSuccess(false);
        r.setCode(ResultCode.FORBIDDEN);
        r.setMessage("权限不足");
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
        this.setCode(code);
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

    public void setData(Map<String, Object> newData) {
        this.data = newData;
    }
}
