package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @title CorsConfig
 * @description
 * @author wbq
 * @version 1.0
 * @create 2023/9/26 15:57
 */

@Configuration
public class CorsConfig {

    /**
     * 跨域过滤.
     */
    @Bean
    public CorsWebFilter corsFilter() {
        return new CorsWebFilter(crosBase());
    }

    /**
     * 跨域过滤.
     */
    public UrlBasedCorsConfigurationSource crosBase() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
