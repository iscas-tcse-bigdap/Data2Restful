package com.iscas.k8scli.response;

public class ErrorResponse extends Result {

    public ErrorResponse(Integer code, String message) {
        super(code, message, false, null);
    }

}
