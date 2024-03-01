package com.iscas.apiservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author wbq
 * @version 1.0
 * @title Swagger2Config
 * @description
 * @create 2023/12/25 14:36
 */

@Configuration
public class SwaggerConfig {
    /**
     * 配置接口文档描述信息.
     */
    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI()
            .info(new Info()
                .title("数据中台项目接口文挡")
                .contact(new Contact().name("wbq")
                    .email("wangbiqiang22@mails.uacs.edu.cn")
                    .url("https://blog.csdn.net/weixin_43843699"))
                .version("1.0")
                .description("本文档描述数据中台项目提供的核心接口定义")
                .license(new License().name("Apache 2.0")));
    }
}
