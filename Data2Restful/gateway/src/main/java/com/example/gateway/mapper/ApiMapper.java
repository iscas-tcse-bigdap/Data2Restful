package com.example.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gateway.pojo.Api;
import org.apache.ibatis.annotations.Mapper;

/**
 *@title ApiMapper
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/13 10:04
 */
@Mapper
public interface ApiMapper extends BaseMapper<Api> {
}
