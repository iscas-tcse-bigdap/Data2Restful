package cn.iscas.userauth.handler;

import cn.iscas.userauth.utils.Response;
import com.alibaba.fastjson.JSON;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *@title AccessDeniedHandlerImpl
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/24 19:58
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private static final int SUCCESS_CODE = 200;
    /**
     * 拒绝访问异常处理.
     * @param request that resulted in an <code>AccessDeniedException</code>
     * @param response so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 处理异常
        try {
            response.setStatus(SUCCESS_CODE);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(Response.forbidden().message("您的权限不足")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
