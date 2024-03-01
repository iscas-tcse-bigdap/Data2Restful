package com.example.gateway.filter;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.example.gateway.pojo.Group;
import com.example.gateway.pojo.KeyAuth;
import com.example.gateway.utils.JwtUtil;
import com.example.gateway.utils.Response;
import com.example.gateway.utils.exception.AccessForbiddenException;
import com.example.gateway.utils.exception.AccessUnauthorizedException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ClassName: PluginFilter
 * @Description:
 * @Author: wbq
 * @Date: 2023/10/24 13:43
 */
@Component
@Slf4j
@SuppressWarnings("unchecked")
public class PluginFilter implements GlobalFilter, Ordered {
    private static final int SUCCESS_CODE = 20000;
    /**
     * gateway插件机制配置.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("gateway插件机制 -> execute globalFilter of plugin");
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();
        final String[] pathSplit = Arrays.stream(path.split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);
        // If the request does not access the published data service, it will be allowed directly.
        if (!pathSplit[0].equals("svc")) {
            return chain.filter(exchange);
        }
        ParameterizedTypeReference<Group> groupTypeRef = new ParameterizedTypeReference<Group>() { };
        ParameterizedTypeReference<Response> responseTypeRef = new ParameterizedTypeReference<Response>() { };
        return webClientGet(URI.create("localhost:8081/api/svc/group/groupByName?groupName=" + pathSplit[1]),
            groupTypeRef)
            .flatMap(group -> Mono.zip(Mono.just(group), webClientGet(
                URI.create("localhost:8081/api/svc/group/acl?groupId=" + group.getGroupId()), responseTypeRef)))
            .flatMap(tuple -> doAcl(tuple, exchange))
            .flatMap(tuple -> doKeyAuth(tuple, request))
            .flatMap(response -> chain.filter(exchange));
    }

    private Mono<Tuple3<Group, Response, Response>> doAcl(Tuple2<Group, Response> tuple, ServerWebExchange exchange) {
        Group group = tuple.getT1();
        Response aclRes = tuple.getT2();
        if (aclRes.getData().get("aclStatus") != null && (Integer) aclRes.getData().get("aclStatus") == 1) {
            List<List<String>> aclList = convertToAclList((LinkedHashMap<String, Object>) aclRes.getData().get("acl"));
            List<String> whitelistUser = aclList.get(0);
            List<String> blacklistUser = aclList.get(1);
            log.warn("whitelistUser:{}", whitelistUser);
            log.info("blacklistUser:{}", blacklistUser);
            String token = exchange.getRequest().getHeaders().getFirst(org.apache.http.HttpHeaders.AUTHORIZATION);
            if (StringUtils.isNotBlank(token)) {
                try {
                    Claims claims = JwtUtil.parseJWT(token);
                    String userid = claims.getSubject();
                    if (whitelistUser.contains(userid)) {
                        return Mono.zip(Mono.just(group), Mono.just(Response.pass()), Mono.just(Response.ok()));
                    }
                    if (blacklistUser.contains(userid)) {
                        return Mono.error(new AccessForbiddenException("您已被限制访问该数据服务"));
                    }
                } catch (Exception e) {
                    return Mono.error(new AccessUnauthorizedException("token无法正常解析：" + e.getMessage()));
                }
            }

        }
        // There is no need for ‘else’ here because plugin judgment can be performed and there is no login,
        // indicating that the currently accessed interface must be a public interface.
        ParameterizedTypeReference<Response> responseTypeRef = new ParameterizedTypeReference<Response>() { };
        return Mono.zip(Mono.just(group), Mono.just(Response.ok()),
            webClientGet(URI.create("localhost:8081/api/svc/group/keyAuth?groupId=" + group.getGroupId()),
                responseTypeRef));
    }

    private Mono<Response> doKeyAuth(Tuple3<Group, Response, Response> tuple, ServerHttpRequest request) {
        Group group = tuple.getT1();
        Response resultPast = tuple.getT2();
        if (resultPast.getCode() != SUCCESS_CODE) {
            return Mono.just(resultPast);
        }
        Response keyAuthRes = tuple.getT3();
        if (keyAuthRes.getData().get("keyAuth") == null) {
            return Mono.just(Response.pass());
        }
        KeyAuth keyAuth = convertToKeyAuth((LinkedHashMap<String, Object>) keyAuthRes.getData().get("keyAuth"));
        if (!(group.getKeyAuthStatus() == 1 && keyAuth.getStatus() == 1)) {
            return Mono.just(resultPast);
        }
        return authenticateKeyAuth(request.getHeaders(), keyAuth);
    }

    private Mono<Response> authenticateKeyAuth(HttpHeaders headers, KeyAuth keyAuth) {
        if (headers.containsKey(keyAuth.getKeyName())) {
            List<String> list = headers.get(keyAuth.getKeyName());
            String code = encode(list.get(0));
            if (code.equals(keyAuth.getKeyPwd())) {
                log.info("Key-auth插件鉴权成功");
                return Mono.just(Response.ok());
            } else {
                return Mono.error(new AccessForbiddenException("Key-auth插件鉴权失败，key_pwd错误"));
            }
        } else {
            return Mono.error(new AccessForbiddenException("Key-auth插件鉴权失败，header中没有对应key_name"));
        }
    }

    private static <T> Mono<T> webClientGet(URI uri, ParameterizedTypeReference<T> responseType) {
        return WebClient.builder().build().get().uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .retrieve()
            .bodyToMono(responseType);
    }

    private static String encode(String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest1 = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest1);
    }

    public static KeyAuth convertToKeyAuth(LinkedHashMap<String, Object> map) {
        return new KeyAuth((int) map.get("keyAuthId"), (int) map.get("groupId"), (String) map.get("keyName"),
            (String) map.get("keyPwd"), (int) map.get("status"));
    }

    private List<List<String>> convertToAclList(LinkedHashMap<String, Object> acl) {
        List<LinkedHashMap<String, String>> whitelist = (List<LinkedHashMap<String, String>>) acl.get("whiteList");
        List<LinkedHashMap<String, String>> blacklist = (List<LinkedHashMap<String, String>>) acl.get("blackList");
        List<String> whitelistUser = new ArrayList<>();
        if (whitelist != null) {
            for (LinkedHashMap<String, String> whitelistItem : whitelist) {
                whitelistUser.add(String.valueOf(whitelistItem.get("aclUserId")));
            }
        }
        List<String> blacklistUser = new ArrayList<>();
        if (blacklist != null) {
            for (LinkedHashMap<String, String> blacklistItem : blacklist) {
                blacklistUser.add(String.valueOf(blacklistItem.get("aclUserId")));
            }
        }
        List<List<String>> res = new ArrayList<>();
        res.add(whitelistUser);
        res.add(blacklistUser);
        return res;
    }


    /**
     * @Description: 这里需要在gateway进行路由转发之前根据/svc路径判断是否需要经过插件验证
     * 经测试，此处优先级至少设置为 1。
     * @Author: wbq
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
