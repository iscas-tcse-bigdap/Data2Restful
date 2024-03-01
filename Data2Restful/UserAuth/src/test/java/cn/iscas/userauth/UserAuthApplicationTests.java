package cn.iscas.userauth;

import cn.iscas.userauth.mapper.MenuMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserAuthApplicationTests {
    @Autowired
    private MenuMapper menuMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSelectPermsByUserId() {
        System.out.println(menuMapper.selectPermsByUserId(2));
    }

}
