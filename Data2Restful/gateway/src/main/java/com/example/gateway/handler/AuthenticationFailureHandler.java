package com.example.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.example.gateway.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * @author:JZ
 * @date:2020/5/21
 */
@Slf4j
@Component
public class AuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    /**
     * 鉴权失败.
     */
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
        log.warn("鉴权失败");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        // 设置返回信息
        HttpHeaders headers = response.getHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        String responseJson = JSON.toJSONString(Response.error().message("鉴权失败"));
        DataBuffer dataBuffer = null;
        try {
            dataBuffer = response.bufferFactory().wrap(responseJson.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return response.writeWith(Mono.just(dataBuffer));
    }

}
