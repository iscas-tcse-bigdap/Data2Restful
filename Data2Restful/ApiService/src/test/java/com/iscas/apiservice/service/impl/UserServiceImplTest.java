package com.iscas.apiservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author wbq
 * @version 1.0
 * @title UserServiceImplTest
 * @description
 * @create 2023/12/14 13:53
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("DatabaseServiceImplTest test")
@RunWith(MockitoJUnitRunner.class)
@Rollback
@Slf4j
class UserServiceImplTest {

    @Autowired
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInputAdviseUserList() {
        Assertions.assertNotNull(userServiceImpl.inputAdviseUserList());
    }
}
