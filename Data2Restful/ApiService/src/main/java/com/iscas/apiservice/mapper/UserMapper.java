package com.iscas.apiservice.mapper;

import com.iscas.apiservice.pojo.controllerToWeb.AclUserIdName;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title UserMapper
 * @description
 * @create 2023/11/14 13:31
 */

@Mapper
@Service
public interface UserMapper {
    @Select("select user_id, username as value from user")
    List<AclUserIdName> getUserList();
}
