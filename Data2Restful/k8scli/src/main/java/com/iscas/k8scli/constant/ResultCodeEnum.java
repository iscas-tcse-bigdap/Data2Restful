package com.iscas.k8scli.constant;

import lombok.Getter;

/**
 * @Author wzc
 * @Description 统一管理请求码/状态码/异常吗
 * @Date 20:05 2023/4/19
 **/

@Getter
public enum ResultCodeEnum {

    SUCCESS(true, 20000, "成功"),
    UNKNOWN_REASON(false, 20001, "未知错误"),
    INVALIDREQUEST(false, 20002, "无效请求"),
    INVALIDTOKEN(false, 22001, "无效token"),
    FORBIDDENACCESS(false, 22003, "无效凭证"),
    ILLEGALPASSWARD(false, 22002, "密码错误"),
    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
    JSON_PARSE_ERROR(false, 21002, "json解析异常"),
    PARAM_ERROR(false, 21003, "参数不正确"),
    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),
    EXCEL_DATA_IMPORT_ERROR(false, 21005, "Excel数据导入错误"),
    ILLEGALARITHMETIC(false, 21006, "算数异常");

    private boolean success;
    private Integer code;
    private String message;
    private Object data;


    ResultCodeEnum(Boolean newSuccess, Integer newCode, String newMessage) {
        this.success = newSuccess;
        this.code = newCode;
        this.message = newMessage;
    }
}
