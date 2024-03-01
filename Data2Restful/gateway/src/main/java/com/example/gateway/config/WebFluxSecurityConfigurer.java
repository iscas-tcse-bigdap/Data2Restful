package com.example.gateway.config;

import com.example.gateway.filter.JwtSecurityContextRepository;
import com.example.gateway.handler.AuthenticationFailureHandler;
import com.example.gateway.handler.AuthenticationSuccessHandler;
import com.example.gateway.handler.ShopHttpBasicServerAuthenticationEntryPoint;
import com.example.gateway.service.ApiService;
import com.example.gateway.service.UserService;
import com.example.gateway.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

import java.util.Arrays;

/*
 *@title WebFluxSecurityConfigurer
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/6 14:16
 */
@Slf4j
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxSecurityConfigurer {

    /**
     * 加载用户配置.
     */
    @Bean
    public ReactiveAuthenticationManager authenticationManager(UserService userService) {
        log.info("加载security 用户配置....");
        return new UserDetailsRepositoryReactiveAuthenticationManager(userService);
    }

    //不验证的url
    private String[] antPatterns = "/user/login,/api/svc/**,/api/k8s/**".split(",");


    /**
     * 鉴权成功处理器.
     */
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    /**
     * 登陆验证失败处理器.
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    /**
     * 未登录访问资源时的处理类，若无此处理类，前端页面会弹出登录窗口.
     */
    @Autowired
    private ShopHttpBasicServerAuthenticationEntryPoint shopHttpBasicServerAuthenticationEntryPoint;

    /**
     * 加载security 权限配置.
     */
    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        log.info("加载security 权限配置....");

        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec = http
                .csrf().disable()
                .cors().configurationSource(new CorsConfig().crosBase()).and()
                .httpBasic().disable()
                .securityContextRepository(new JwtSecurityContextRepository())   // 自己实现的认证方式
                .authorizeExchange();

        authorizeExchangeSpec    //请求进行授权
                .pathMatchers(antPatterns).permitAll()
                .matchers((serverWebExchange) -> {   //自定义验证授权规则，如果此方法成立，那么配合permitAll()方法进行放行逻辑
                    try {
                        ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();  //获取请求对象
                        String path = serverHttpRequest.getURI().getPath();
                        String[] pathSplit = path.split("/");
                        pathSplit = Arrays.stream(pathSplit)
                                .filter(s -> !s.isEmpty())
                                .toArray(String[]::new);
                        if (pathSplit[0].equals("svc")) {
                            ApiService apiServiceBean = BeanUtil.getBean(ApiService.class);
                            if (apiServiceBean.getApiPublicOrNot(pathSplit[2])) {
                                log.info("gateway权鉴security配置->放行公开数据服务接口,path:{}", path);
                                return ServerWebExchangeMatcher.MatchResult.match();
                            }
                        }
                        return ServerWebExchangeMatcher.MatchResult.notMatch();
                    } catch (Exception e) {
                        return ServerWebExchangeMatcher.MatchResult.notMatch();
                    }
                }).permitAll()
                .anyExchange().authenticated();  //都需要身份认证;

        SecurityWebFilterChain chain = http.build();
        return chain;
    }

}
