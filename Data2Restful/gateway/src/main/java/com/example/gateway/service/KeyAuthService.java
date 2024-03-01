package com.example.gateway.service;

import com.example.gateway.pojo.KeyAuth;

/*
 *@title KeyAuthMapper
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/24 17:14
 */
public interface KeyAuthService {
    KeyAuth getKeyAuth(int groupId);
}
