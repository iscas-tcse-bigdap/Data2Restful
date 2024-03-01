package com.iscas.k8scli.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    private static final long CORS_MAX_AGE = 3600L;
    private static final Logger LOGGER = LoggerFactory.getLogger(CorsConfig.class);

    @Value("${cors.allowed.origins:}")
    private String origins;


    private CorsConfiguration corsConfiguration() {
        LOGGER.info(String.valueOf(origins.split(",")));
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(origins.split(",")));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(CORS_MAX_AGE);
        return corsConfiguration;
    }

    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/META-INF/resources/static/");

    }
}
