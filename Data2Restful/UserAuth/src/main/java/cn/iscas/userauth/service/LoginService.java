package cn.iscas.userauth.service;


import cn.iscas.userauth.pojo.User;
import cn.iscas.userauth.utils.Response;

/**
 *@title LoginService
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 15:15
 */
public interface LoginService {
    Response login(User user);

    Response logout();
}
