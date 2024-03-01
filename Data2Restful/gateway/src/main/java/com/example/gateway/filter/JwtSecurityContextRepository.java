package com.example.gateway.filter;

import com.example.gateway.service.UserService;
import com.example.gateway.utils.BeanUtil;
import com.example.gateway.utils.exception.AccessUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *@title JwtSecurityContextRepository
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/6 13:34
 */
@Component
@Slf4j
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {
    @Autowired
    private UserService userService;


    /**
     * save.
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    /**
     * gateway权鉴security配置.
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();
        log.info("gateway权鉴security配置 -> 加载token：JwtSecurityContextRepository：path：{}", path);
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(token)) {
            try {
                UserService userServiceBean = BeanUtil.getBean(UserService.class);
                Mono<UserDetails> userDetailsMono = userServiceBean.findByUserNameWithToken(token);
                Mono<SecurityContext> mono = userDetailsMono.map(s -> {
                    UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(s.getUsername(),
                        null, s.getAuthorities());  //验证用户不再填入密码
                    SecurityContextImpl data = new SecurityContextImpl();
                    data.setAuthentication(t);
                    return data;
                });
                return mono;
            } catch (AccountExpiredException e) {
                return Mono.error(new AccountExpiredException("登录用户已过期"));
            } catch (Exception e) {
                return Mono.error(new AccessUnauthorizedException("token不能正常解析"));
            }
        } else {
            return Mono.error(new AccessUnauthorizedException("请求的AUTHORIZATION参数为空"));
        }
    }
}
