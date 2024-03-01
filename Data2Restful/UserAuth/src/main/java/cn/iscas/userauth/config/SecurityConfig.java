package cn.iscas.userauth.config;

import cn.iscas.userauth.filter.JwtAuthenticationTokenFilter;
import cn.iscas.userauth.handler.AccessDeniedHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *@title SecurityConfig
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 14:43
 */


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();   // 密码加密
    }

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    /**
     * security configure.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // 关闭 csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // 不通过session获取SecurityContext
                .and().authorizeRequests()
                .antMatchers("/user/login").anonymous()  // 允许匿名访问登录接口
                .anyRequest().authenticated();  // 其他都需要鉴权

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)    // 认证失败处理器
                .accessDeniedHandler(accessDeniedHandler);             // 授权失败处理器

    }

    /**
     * 重写该方法进行用户认证.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();   // 重写该方法进行用户认证
    }
}
