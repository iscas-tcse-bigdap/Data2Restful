package com.example.gateway.utils.exception;

import com.example.gateway.utils.Response;

public final class AccessForbiddenException extends AccessException {
    private static final int FORBIDDEN_CODE = 403;
    public AccessForbiddenException(String message) {
        this.setCode(FORBIDDEN_CODE);
        this.setMessage(message);
    }

    @Override
    public Response toResponse() {
        return Response.forbidden().message(this.getMessage());
    }
}
