package cn.iscas.userauth.service.impl;

import cn.iscas.userauth.pojo.LoginUser;
import cn.iscas.userauth.pojo.User;
import cn.iscas.userauth.service.LoginService;
import cn.iscas.userauth.utils.JwtUtil;
import cn.iscas.userauth.utils.RedisCache;
import cn.iscas.userauth.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 *@title LoginServiceImpl
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 15:15
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * LOGIN.
     * @param user user info
     * @return result
     */
    @Override
    public Response login(User user) {
        // 使用 authenticationManager 进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 如果没通过，给出提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        // 如果通过了生成jwt，并在Response中返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = String.valueOf(loginUser.getUser().getUserId());
        String jwt = JwtUtil.createJWT(userid);

        // 存入redis， userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);

        return Response.ok().data("token", jwt);
    }

    /**
     * LOGOUT
     * @return result
     */
    @Override
    public Response logout() {
        // 获取 securityContextHolder 中的 userid
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
            SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        int userId = loginUser.getUser().getUserId();
        // 删除 redis 中的数据
        redisCache.deleteObject("login:" + userId);
        return Response.ok().message("注销成功");
    }
}
