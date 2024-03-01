package cn.iscas.userauth.controller;

import cn.iscas.userauth.pojo.User;
import cn.iscas.userauth.service.LoginService;
import cn.iscas.userauth.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *@title LoginController
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 15:10
 */

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * login.
     * @param user 用户信息
     * @return 登录结果
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Response login(@RequestBody User user) {
        return loginService.login(user);
    }

    /**
     * 登出.
     * @return result
     */
    @RequestMapping(path = "/logout")
    public Response logout() {
        return loginService.logout();
    }
}
