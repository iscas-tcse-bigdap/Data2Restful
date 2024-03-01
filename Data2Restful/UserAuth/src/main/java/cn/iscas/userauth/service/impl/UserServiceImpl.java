package cn.iscas.userauth.service.impl;

import cn.iscas.userauth.mapper.MenuMapper;
import cn.iscas.userauth.mapper.UserMapper;
import cn.iscas.userauth.pojo.LoginUser;
import cn.iscas.userauth.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 *@title UserServiceImpl
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 14:19
 */
@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * @param username the username identifying the user whose data is required.
     * @return 用户信息
     * @throws UsernameNotFoundException 用户名不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        List<String> list = menuMapper.selectPermsByUserId(user.getUserId());
        return new LoginUser(user, list);
    }
}
