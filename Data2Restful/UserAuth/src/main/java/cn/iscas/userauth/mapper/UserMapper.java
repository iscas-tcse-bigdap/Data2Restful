package cn.iscas.userauth.mapper;

import cn.iscas.userauth.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *@title UserMapper
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 13:56
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
