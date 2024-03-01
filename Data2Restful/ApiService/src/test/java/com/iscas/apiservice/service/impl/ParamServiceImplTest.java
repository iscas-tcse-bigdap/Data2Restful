package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.pojo.Parameter;
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

import javax.transaction.Transactional;
import java.util.Arrays;

/**
 * @author wbq
 * @version 1.0
 * @title ParamServiceImplTest
 * @description
 * @create 2023/12/14 13:42
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("DatabaseServiceImplTest test")
@RunWith(MockitoJUnitRunner.class)
@Rollback
@Slf4j
class ParamServiceImplTest {

    @Autowired
    @InjectMocks
    ParamServiceImpl paramServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void testAddParam() {
        Assertions.assertTrue(paramServiceImpl.addParam(new Parameter("name", "type", "defaultValue", "name")) > 0);
    }

    @Test
    @Transactional
    void testAddParams() {
        Assertions.assertTrue(paramServiceImpl.addParams(Arrays.<Parameter>asList(
            new Parameter("name", "type", "defaultValue", "title"),
            new Parameter("name", "type", "defaultValue", "title"))) > 0);
    }

    @Test
    void testGetParamsByApiId() {
        Assertions.assertNotNull(paramServiceImpl.getParamsByApiId(2));
    }
}
