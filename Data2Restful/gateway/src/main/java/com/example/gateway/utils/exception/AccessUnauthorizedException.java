package com.example.gateway.utils.exception;

import com.example.gateway.utils.Response;

/**
 * @author wbq
 * @version 1.0
 * @title AccessUnauthorizedException
 * @description
 * @create 2023/11/9 10:26
 */

public final class AccessUnauthorizedException extends AccessException {
    private static final int UNAUTHORIZED_CODE = 401;
    public AccessUnauthorizedException(String message) {
        this.setCode(UNAUTHORIZED_CODE);
        this.setMessage(message);
    }
    @Override
    public Response toResponse() {
        return Response.unauthorized().message(this.getMessage());
    }
}
