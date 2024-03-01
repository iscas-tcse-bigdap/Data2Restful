package com.iscas.k8scli.exception;

/**
 * @ClassName: SimpleException
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/22 16:02
 */
public class SimpleException extends BaseException {
    public SimpleException(int code, String errorMessage) {
        super(code, errorMessage);
    }
}
