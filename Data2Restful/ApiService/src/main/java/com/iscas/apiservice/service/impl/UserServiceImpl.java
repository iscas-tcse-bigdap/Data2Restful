package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.mapper.UserMapper;
import com.iscas.apiservice.service.UserService;
import com.iscas.apiservice.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wbq
 * @version 1.0
 * @title UserServiceImpl
 * @description
 * @create 2023/11/14 13:30
 */

@Service
public final class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Response inputAdviseUserList() {
        return Response.ok().data("userList", userMapper.getUserList());
    }
}
