package com.example.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.example.gateway.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * 鉴权成功处理器
 * @author:JZ
 * @date:2020/5/21
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {

    public AuthenticationSuccessHandler() { }

    /**
     * 认证成功.
     */
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        log.info("用户：{} 登陆成功");
        // 设置返回信息
        HttpHeaders headers = response.getHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        String responseJson = JSON.toJSONString(Response.ok());
        DataBuffer dataBuffer = null;
        try {
            dataBuffer = response.bufferFactory().wrap(responseJson.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response.writeWith(Mono.just(dataBuffer));
    }

}
