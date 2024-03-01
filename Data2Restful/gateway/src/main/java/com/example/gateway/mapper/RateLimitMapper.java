package com.example.gateway.mapper;

import com.example.gateway.pojo.RateLimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title RateLimitMapper
 * @description
 * @create 2023/11/2 11:05
 */

@Mapper
public interface RateLimitMapper {

    @Select("select drl.* , apigroup.name from plugin_dynamic_rate_limit drl left join apigroup on drl.group_id = "
        + "apigroup.group_id where drl.status = 1;")
    List<RateLimit> getRateLimitInfo();

    @Select("select apigroup.name from plugin_dynamic_rate_limit drl left join apigroup on drl.group_id = apigroup"
         + ".group_id;")
    List<String> getRateLimitedSvc();
}
