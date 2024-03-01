package com.iscas.apiservice;

import com.iscas.apiservice.mapper.GroupMapper;
import com.iscas.apiservice.mapper.PluginMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiServiceApplicationTests {
    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private PluginMapper pluginMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetPlugin() {
        System.out.println(groupMapper.getGroupByName("group1"));
    }

    @Test
    void test() {
        System.out.println(groupMapper.getPlugin(13));
    }

}
