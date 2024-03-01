package com.iscas.k8scli.response;

import com.iscas.k8scli.constant.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//设置统一资源返回结果集
@Data
@ApiModel(value = "全局统一返回结果")
public class Result {
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;

    @ApiModelProperty(value = "返回码")
    private  Integer code;
    @ApiModelProperty(value = "返回消息")
    private  String message;
    @ApiModelProperty(value = "是否成功")
    private  Boolean success;
    @ApiModelProperty(value = "返回数据")
    private Object data;

    public static Result succeed(Object data) {
        return new Result(SUCCESS_CODE, "", true, data);
    }

    public static Result failed(Object data) {
        return new Result(ERROR_CODE, "", false, data);
    }

    public static Result failed(int code, Object data) {
        return new Result(code, "", false, data);
    }

    private Result() { }

    public Result(Integer newCode, String newMessage, Boolean newSuccess, Object newData) {
        this.code = newCode;
        this.message = newMessage;
        this.success = newSuccess;
        this.data = newData;
    }

    //返回成功的结果集
    public static Result success() {
        Result r = new Result();
        r.setSuccess(ResultCodeEnum.SUCCESS.isSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    //返回带参的成功结果集
    public static Result success(Object data) {
        Result r = new Result();
        r.setSuccess(ResultCodeEnum.SUCCESS.isSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        r.setData(data);
        return r;
    }

    //返回失败的结果集
    public static Result failed() {
        Result r = new Result();
        r.setSuccess(ResultCodeEnum.UNKNOWN_REASON.isSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return r;
    }
    /**
     *
     * @param resultCodeEnum
     * @return
     */
    public static Result setResult(ResultCodeEnum resultCodeEnum) {
        Result r = new Result();
        r.setSuccess(resultCodeEnum.isSuccess());
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMessage());
        return r;
    }

    /**
     * 设置 success
     */
    public Result success(Boolean newSuccess) {
        this.setSuccess(newSuccess);
        return this;
    }

    /**
     * 设置 message
     */
    public Result message(String newMessage) {
        this.setMessage(newMessage);
        return this;
    }

    /**
     * 设置 code
     */
    public Result code(Integer newCode) {
        this.setCode(newCode);
        return this;
    }

    /**
     * 设置 data
     */
    public Result data(Object newData) {
        this.data = newData;
        return this;

    }

    /**
     * 是否成功？
     */
    public boolean isSuccess() {
        return success;
    }

}
