package com.example.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gateway.mapper.GroupMapper;
import com.example.gateway.pojo.Group;
import com.example.gateway.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/*
 *@title GroupServiceImpl
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/24 16:40
 */

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupMapper groupMapper;

    /**
     * 根据组名称获取数据服务.
     */
    @Override
    public Group getGroup(String name) {
        Group group = groupMapper.selectOne(new LambdaQueryWrapper<Group>().eq(Group::getName, name));
        if (Objects.isNull(group)) {
            throw new RuntimeException("不存在名为" + name + "的数据服务");
        }
        return group;
    }
}
