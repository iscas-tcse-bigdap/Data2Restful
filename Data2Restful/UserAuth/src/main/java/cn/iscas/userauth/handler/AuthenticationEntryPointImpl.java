package cn.iscas.userauth.handler;

import cn.iscas.userauth.utils.Response;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @title AuthenticationEntryPointImpl
 * @description  认证失败的异常处理
 * @author wbq
 * @version 1.0
 * @create 2023/10/24 19:22
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private static final int SUCCESS_CODE = 200;
    /**
     * 认证过期异常处理.
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 处理异常
        try {
            response.setStatus(SUCCESS_CODE);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(Response.unauthorized().message("用户认证失败")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
