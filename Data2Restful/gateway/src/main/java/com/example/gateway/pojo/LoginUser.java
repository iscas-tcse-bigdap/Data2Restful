package com.example.gateway.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/*
 *@title LoginUser
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 14:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {
    private User user;

    /**
     * getAuthorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 用户密码.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 用户名.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 账户未过期？.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户未被锁定？.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 认证未过期？.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 允许访问？.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
