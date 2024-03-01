package cn.iscas.userauth.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *@title LoginUser
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 14:28
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private User user;

    private List<String> permissions;

    public LoginUser(User newUser, List<String> newPermissions) {
        this.user = newUser;
        this.permissions = newPermissions;
    }

    //存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    /**
     * 获取权限信息.
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null) {
            return authorities;
        }
        // 把permissions中的string类型的权限信息封装成SimpleGrantedAuthority对象
        /*
        List<GrantedAuthority> newList = new ArrayList<>();
        for (String permission : permissions) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
            newList.add(authority);
        }*/
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    /**
     * @return getPassword.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * @return PWD
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * @return 是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return 账户锁定？
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 认证过期？
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 允许访问？
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
