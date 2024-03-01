package com.example.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * @author wbq
 * @version 1.0
 * @title HttpResponse
 * @description
 * @create 2023/11/8 15:58
 */

public class HttpResponseUtil implements ServerHttpResponse {

    public static Mono<Void> forbidden(ServerHttpResponse response, Response body) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.FORBIDDEN);
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = json.getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    public static Mono<Void> unauthorized(ServerHttpResponse response, Response body) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = json.getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    public static Mono<Void> error(ServerHttpResponse response, Response body) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = json.getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * @param status the HTTP status as an {@link HttpStatus} enum value
     */
    @Override
    public boolean setStatusCode(HttpStatus status) {
        return false;
    }

    /**
     * @return the HTTP status as an {@link HttpStatus} enum value
     */
    @Override
    public HttpStatus getStatusCode() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public MultiValueMap<String, ResponseCookie> getCookies() {
        return null;
    }

    @Override
    public void addCookie(ResponseCookie cookie) {

    }

    /**
     * @return
     */
    @Override
    public DataBufferFactory bufferFactory() {
        return null;
    }

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {

    }

    /**
     * @return
     */
    @Override
    public boolean isCommitted() {
        return false;
    }

    /**
     * @param body the body content publisher
     */
    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        return null;
    }

    /**
     * @param body the body content publisher
     */
    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Mono<Void> setComplete() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public HttpHeaders getHeaders() {
        return null;
    }
}
