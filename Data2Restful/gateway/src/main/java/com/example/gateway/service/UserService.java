package com.example.gateway.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gateway.mapper.UserMapper;
import com.example.gateway.pojo.LoginUser;
import com.example.gateway.pojo.User;
import com.example.gateway.utils.JwtUtil;
import com.example.gateway.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @title LoginServiceImpl
 * @description
 * @author wbq
 * @version 1.0
 * @create 2023/10/6 13:19
 */
@Service
public class UserService implements ReactiveUserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * @param username the username to look up
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // todo: 查询对应的权限信息
        LoginUser loginUser = new LoginUser(user);
        return Mono.just(loginUser);
    }

    /**
     * @param token
     * @return
     * @throws Exception
     */
    public Mono<UserDetails> findByUserNameWithToken(String token) throws Exception {
        boolean userExists = queryCacheUserExists(token);
        if (userExists) {
            Claims claims = JwtUtil.parseJWT(token);
            User user = userMapper.selectById(Integer.parseInt(claims.getSubject()));
            UserDetails userDetails = new LoginUser(user);
            return Mono.just(userDetails);
        } else {
            throw new AccountExpiredException("前端传来的jwt已失效");
        }
    }

    private boolean queryCacheUserExists(String jwt) throws Exception {
        Claims claims = JwtUtil.parseJWT(jwt);
        String userid = claims.getSubject();
        String redisKey = "login:" + userid;
        Object cacheObject = redisCache.getCacheObject(redisKey);
        return Objects.nonNull(cacheObject);
    }

}
