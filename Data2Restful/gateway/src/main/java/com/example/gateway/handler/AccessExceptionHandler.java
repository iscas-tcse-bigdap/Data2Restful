package com.example.gateway.handler;

import com.example.gateway.utils.exception.AccessForbiddenException;
import com.example.gateway.utils.exception.AccessUnauthorizedException;
import com.example.gateway.utils.HttpResponseUtil;
import com.example.gateway.utils.Response;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author wbq
 * @version 1.0
 * @title AccessForbiddenHandler
 * @description
 * @create 2023/11/8 21:32
 */

@Component
public class AccessExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * 异常处理.
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof AccessForbiddenException) {
            return handleAccessForbiddenException(exchange, (AccessForbiddenException) ex);
        } else if (ex instanceof AccessUnauthorizedException) {
            return handleAccessUnauthorizedException(exchange, (AccessUnauthorizedException) ex);
        } else if (ex instanceof AccountExpiredException) {
            return HttpResponseUtil.unauthorized(exchange.getResponse(), Response.unauthorized().message("账号已过期"));
        }
        throw new RuntimeException(ex);
//        return HttpResponseUtil.forbidden(exchange.getResponse(), Response.error()
//        .message("未知错误:" + ex.getMessage()));
    }

    /**
     * 禁止访问的异常处理.
     */
    public Mono<Void> handleAccessForbiddenException(ServerWebExchange exchange, AccessForbiddenException e) {
        return HttpResponseUtil.forbidden(exchange.getResponse(), e.toResponse());
    }

    /**
     * 未认证的异常处理.
     */
    public Mono<Void> handleAccessUnauthorizedException(ServerWebExchange exchange, AccessUnauthorizedException e) {
        return HttpResponseUtil.unauthorized(exchange.getResponse(), e.toResponse());
    }
}
