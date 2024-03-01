package com.example.gateway.utils.exception;

import com.example.gateway.utils.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wbq
 * @version 1.0
 * @title AccessException
 * @description
 * @create 2023/11/8 22:37
 */

@Getter
@Setter
public abstract class AccessException extends RuntimeException {
    private int code;
    private String message;
    private Throwable cause;

    public AccessException() {
    }

    public AccessException(String newMessage) {
        super(newMessage);
    }

    public AccessException(String newMessage, Throwable newCause) {
        super(newMessage, newCause);
    }

    public AccessException(Throwable newCause) {
        super(newCause);
    }

    public AccessException(String newMessage, Throwable newCause, boolean enableSuppression,
                           boolean writableStackTrace) {
        super(newMessage, newCause, enableSuppression, writableStackTrace);
    }
    protected abstract Response toResponse();
}

