package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.feign.K8scliFeign;
import com.iscas.apiservice.pojo.*;
import com.iscas.apiservice.pojo.controllerToWeb.Acl;
import com.iscas.apiservice.pojo.dbTemplate.CreateAcl;
import com.iscas.apiservice.pojo.dbTemplate.CreateAclDetailItem;
import com.iscas.apiservice.pojo.feignToK8scli.CreateIngressInfo;
import com.iscas.apiservice.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * @author wbq
 * @version 1.0
 * @title GroupServiceImplTest
 * @description
 * @create 2023/12/13 15:33
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("GroupServiceImplTest test")
@RunWith(MockitoJUnitRunner.class)
@Rollback
@Slf4j
class GroupServiceImplTest {

    private static Date createDateFromDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace(); // 处理解析异常
            return null;
        }
    }

    private static final Group GROUP1 =
        new Group(1, 1, "group1", "这是一个测试分组", createDateFromDateString("2023-10-17 15:19:44"), 1, 1, 0, 0);

    private static final Group GROUP_WITHOUT_ID =
        new Group(0, 1, "junit-test", "这是一个测试分组", new Date(), 0, 0, 0, 0);

    @InjectMocks
    @Autowired
    GroupServiceImpl groupServiceImpl;
    @Mock
    K8scliFeign k8scliFeign;

    @Before
    public void before() {
        groupServiceImpl.setK8scliFeign(k8scliFeign);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void testCreateGroup() {
        // correct
        when(k8scliFeign.createIngress(any())).thenReturn("success");
        Assertions.assertTrue(groupServiceImpl.createGroup(GROUP_WITHOUT_ID) > 0);
        // createGroup error: without group_name
        Group junitGroup1 = new Group(233, 1, null, "这是一个测试分组", new Date(), 0, 0, 0, 0);
        Assertions.assertFalse(groupServiceImpl.createGroup(junitGroup1) > 0);
        // createIngress error
        when(k8scliFeign.createIngress(any(CreateIngressInfo.class))).thenThrow(new RuntimeException("junit-test"));
        Group junitGroup2 = new Group(233 * 2, 1, "junit-test", "这是一个测试分组", new Date(), 0, 0, 0, 0);
        Assertions.assertEquals(-2, groupServiceImpl.createGroup(junitGroup2));
    }

    @Test
    void testGetGroupList() {
        ArrayList<Group> groupList = groupServiceImpl.getGroupList();
        Assertions.assertNotNull(groupList);
    }

    @Test
    void testGetGroup() {
        Assertions.assertNotNull(groupServiceImpl.getGroup(1));
        Assertions.assertNull(groupServiceImpl.getGroup(2));
    }

    @Test
    void testGetGroupByName() {
        Assertions.assertNotNull(groupServiceImpl.getGroupByName("group1"));
        Assertions.assertNull(groupServiceImpl.getGroupByName("group-junit"));
    }

    @Test
    @Transactional
    void testDeleteGroup() {
        when(k8scliFeign.deleteIngress(anyString())).thenReturn("success");
        when(k8scliFeign.deleteAutoAdjustReplica(anyString())).thenReturn("success");
        // correct situation
        Assertions.assertEquals(Response.ok().message("删除服务分组成功").getMessage(),
            groupServiceImpl.deleteGroup(8, "group3").getMessage());
        // error situation: group not exist
        Assertions.assertFalse(groupServiceImpl.deleteGroup(2, "junitTest").isSuccess());
        // deleteIngress error
        when(k8scliFeign.deleteIngress(anyString())).thenThrow(new RuntimeException("junit-test"));
        Assertions.assertFalse(groupServiceImpl.deleteGroup(1, "group3").isSuccess());
    }

    @Test
    @Transactional
    void testUpdateGroup() {
        Assertions.assertEquals(1, groupServiceImpl.updateGroup(GROUP1));
    }

    @Test
    void testGetPluginList() {
        Assertions.assertNotNull(groupServiceImpl.getPluginList(1).getData());
        Assertions.assertFalse(groupServiceImpl.getPluginList(2).isSuccess());
    }

    @Test
    void testGetUnallocatedPluginList() {
        Assertions.assertNotNull(groupServiceImpl.getUnallocatedPluginList(20));
        Assertions.assertFalse(groupServiceImpl.getUnallocatedPluginList(2).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateKeyAuthPlugin() throws NoSuchAlgorithmException {
        // update 不存在的项不会报错，也不会对任何内容进行更新：
        Assertions.assertTrue(groupServiceImpl.
            updateKeyAuthPlugin(new PluginKeyAuth(1, 1, "key-auth-junit", "123", 1, new Date())).isSuccess());
        Assertions.assertFalse(groupServiceImpl.updateKeyAuthPlugin(new PluginKeyAuth(1)).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateKeyAuthStatus() {
        Response result = groupServiceImpl.
            updateKeyAuthStatus(new PluginKeyAuth(1, 1, "key-auth-junit", "123", 1, new Date()));
        Assertions.assertTrue(result.isSuccess());
    }

    @Test
    @Transactional
    void testDeleteKeyAuthPlugin() {
        Assertions.assertTrue(groupServiceImpl.deleteKeyAuthPlugin(1).isSuccess());
        // todo: 删除不存在的项是否要给出提示
//        Assertions.assertFalse(groupServiceImpl.deleteKeyAuthPlugin(2).isSuccess());
    }

    @Test
    @Transactional
    void testAddKeyAuthPlugin() {
        // 几种情况：1. group不存在； 2. 分组已添加key-auth插件
        // correct situation
        Assertions.assertTrue(groupServiceImpl.
            addKeyAuthPlugin(new PluginKeyAuth(0, 20, "key-auth-junit", "123", 1, new Date())).isSuccess());
        // error situation: group not exist
        Assertions.assertFalse(groupServiceImpl.
            addKeyAuthPlugin(new PluginKeyAuth(0, 2, "key-auth-junit", "123", 1, new Date())).isSuccess());
        // error situation: group already has key-auth plugin
        Assertions.assertFalse(groupServiceImpl.
            addKeyAuthPlugin(new PluginKeyAuth(0, 20, "key-auth-junit", "123", 1, new Date())).isSuccess());
    }

    @Test
    void testGetKeyAuth() {
        Assertions.assertNotNull(groupServiceImpl.getKeyAuth(1).getData().get("keyAuth"));
        Assertions.assertNull(groupServiceImpl.getKeyAuth(2).getData().get("keyAuth"));
    }

    @Test
    void testGetAcl() {
        Assertions.assertFalse(checkAclNull((Acl) groupServiceImpl.getAcl(1).getData().get("acl")));
        Assertions.assertTrue(checkAclNull((Acl) groupServiceImpl.getAcl(2).getData().get("acl")));
    }

    boolean checkAclNull(Acl acl) {
        return acl.getWhiteList() == null && acl.getBlackList() == null;
    }

    @Test
    @Transactional
    void testCreateAclPlugin() {
        // 可能的错误情况： 1. group不存在； 2. 分组已添加acl； 3. 主键重复
        // correct situation
        Assertions.assertTrue(groupServiceImpl.createAclPlugin(new PluginAcl(1, 20, 1, new Date())).isSuccess());
        // error situation: group not exist
        Assertions.assertFalse(groupServiceImpl.createAclPlugin(new PluginAcl(0, 2, 1, new Date())).isSuccess());
        // error situation: group already has acl plugin
        Assertions.assertFalse(groupServiceImpl.createAclPlugin(new PluginAcl(0, 20, 1, new Date())).isSuccess());
        // error situation: acl_id repeated
        Assertions.assertFalse(groupServiceImpl.createAclPlugin(new PluginAcl(1, 20, 1, new Date())).isSuccess());
    }

    @Test
    @Transactional
    void testDeleteAclPlugin() {
        // 可能的错误情况： 1. group不存在； 2. 分组没有acl
        Assertions.assertTrue(groupServiceImpl.deleteAclPlugin(1).isSuccess());
        Assertions.assertFalse(groupServiceImpl.deleteAclPlugin(2).isSuccess());
        Assertions.assertFalse(groupServiceImpl.deleteAclPlugin(20).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateAclPlugin() {
        // 可能的错误情况： 1. acl不存在； 2. 白名单内容id与外层aclid不对应； 3. 黑名单内容id与外层aclid不对应
        Response result = groupServiceImpl.updateAclPlugin(new CreateAcl(2,
            Arrays.asList(new CreateAclDetailItem(1, 2)),
            Arrays.asList(new CreateAclDetailItem(2, 2))));
        Assertions.assertTrue(result.isSuccess());
        // error situation: acl not exist
        Assertions.assertFalse(groupServiceImpl.updateAclPlugin(new CreateAcl(1,
            Collections.singletonList(new CreateAclDetailItem(1, 1)),
            Collections.singletonList(new CreateAclDetailItem(2, 1)))).isSuccess());
        // error situation: acl id not match
        Assertions.assertFalse(groupServiceImpl.updateAclPlugin(new CreateAcl(2,
            Collections.singletonList(new CreateAclDetailItem(1, 1)),
            Collections.singletonList(new CreateAclDetailItem(2, 1)))).isSuccess());
        // error situation: acl id not match
        Assertions.assertFalse(groupServiceImpl.updateAclPlugin(new CreateAcl(2,
            Collections.singletonList(new CreateAclDetailItem(1, 1)),
            Collections.singletonList(new CreateAclDetailItem(2, 1)))).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateAclStatus() {
        Assertions.assertTrue(groupServiceImpl.updateAclStatus(new PluginAcl(2, 1, 0, new Date())).isSuccess());
    }

    @Test
    @Transactional
    void testCreateRateLimitPlugin() {
        // 可能的错误情况： 1. group不存在； 2. 分组已添加rate-limit
        // correct situation
        Assertions.assertTrue(groupServiceImpl.
            createRateLimitPlugin(new PluginDynamicRateLimit(0, 20, 5, 60, 1, new Date())).isSuccess());
        // error situation: group not exist
        Assertions.assertFalse(groupServiceImpl.
            createRateLimitPlugin(new PluginDynamicRateLimit(0, 2, 5, 60, 1, new Date())).isSuccess());
        // error situation: group already has rate-limit plugin
        Assertions.assertFalse(groupServiceImpl.
            createRateLimitPlugin(new PluginDynamicRateLimit(0, 20, 5, 60, 1, new Date())).isSuccess());
    }

    @Test
    @Transactional
    void testDeleteRateLimitPlugin() {
        // 可能的错误情况： 1. 分组不存在； 2. 分组没有rate-limit
        Assertions.assertTrue(groupServiceImpl.deleteRateLimitPlugin(1).isSuccess());
        Assertions.assertFalse(groupServiceImpl.deleteRateLimitPlugin(2).isSuccess());
        Assertions.assertFalse(groupServiceImpl.deleteRateLimitPlugin(20).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateRateLimitPlugin() {
        Assertions.assertTrue(groupServiceImpl.
            updateRateLimitPlugin(new PluginDynamicRateLimit(4, 8, 5, 60, 1, new Date())).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateRateLimitStatus() {
        Assertions.assertTrue(groupServiceImpl.
            updateRateLimitStatus(new PluginDynamicRateLimit(4, 8, 5, 60, 1, new Date())).isSuccess());
    }

    @Test
    @Transactional
    void testCreateDynamicExpansionPlugin() {
        // 可能的错误情况： 1. group不存在； 2. 分组已添加dynamic-expansion
        Assertions.assertTrue(groupServiceImpl.
            createDynamicExpansionPlugin(new PluginDynamicExpansion(0, 20, 2, 5, 1, new Date())).isSuccess());
        // error situation: group not exist
        Assertions.assertFalse(groupServiceImpl.
            createDynamicExpansionPlugin(new PluginDynamicExpansion(0, 2, 2, 5, 1, new Date())).isSuccess());
        // error situation: group already has dynamic-expansion plugin
        Assertions.assertFalse(groupServiceImpl.
            createDynamicExpansionPlugin(new PluginDynamicExpansion(0, 20, 2, 5, 1, new Date())).isSuccess());
    }

    @Test
    @Transactional
    void testDeleteDynamicExpansionPlugin() {
        // 可能的错误情况： 1. 分组不存在； 2. 分组没有dynamic-expansion； 3. k8s集群异常
        when(k8scliFeign.createAutoAdjustReplica(any())).thenReturn("success");
        when(k8scliFeign.deleteAutoAdjustReplica(anyString())).thenReturn("success");
        Assertions.assertTrue(groupServiceImpl.deleteDynamicExpansionPlugin(8).isSuccess());
        Assertions.assertFalse(groupServiceImpl.deleteDynamicExpansionPlugin(2).isSuccess());
        Assertions.assertFalse(groupServiceImpl.deleteDynamicExpansionPlugin(8).isSuccess());
        groupServiceImpl.updateDynamicExpansionStatus(new PluginDynamicExpansion(15, 1, 1, 3, 1, new Date()));
        when(k8scliFeign.deleteAutoAdjustReplica(anyString())).thenThrow(new RuntimeException("junit-test"));
        Assertions.assertFalse(groupServiceImpl.deleteDynamicExpansionPlugin(1).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateDynamicExpansionPlugin() {
        when(k8scliFeign.updateAutoAdjustReplicaNum(any())).thenReturn("success");
        Assertions.assertTrue(groupServiceImpl.
            updateDynamicExpansionPlugin(new PluginDynamicExpansion(3, 8, 1, 3, 1, new Date())).isSuccess());
        when(k8scliFeign.updateAutoAdjustReplicaNum(any())).thenThrow(new RuntimeException("junit-test"));
        Assertions.assertFalse(groupServiceImpl.
            updateDynamicExpansionPlugin(new PluginDynamicExpansion(3, 8, 2, 5, 1, new Date())).isSuccess());
    }

    @Test
    @Transactional
    void testUpdateDynamicExpansionStatus() {
        // 几种错误情况：1. group不存在； 2. 分组没有dynamic-expansion； 3. dynamic-expansion不存在； 4. k8s集群异常
        when(k8scliFeign.createAutoAdjustReplica(any())).thenReturn("success");
        when(k8scliFeign.deleteAutoAdjustReplica(anyString())).thenReturn("success");
        Assertions.assertTrue(groupServiceImpl.
            updateDynamicExpansionStatus(new PluginDynamicExpansion(3, 8, 1, 3, 0, new Date())).isSuccess());
        Assertions.assertTrue(groupServiceImpl.
            updateDynamicExpansionStatus(new PluginDynamicExpansion(3, 8, 1, 3, 1, new Date())).isSuccess());
        // error situation: group not exist
        Assertions.assertFalse(groupServiceImpl.
            updateDynamicExpansionStatus(new PluginDynamicExpansion(3, 2, 1, 3, 1, new Date())).isSuccess());
        // error situation: group not have dynamic-expansion
        Assertions.assertFalse(groupServiceImpl.
            updateDynamicExpansionStatus(new PluginDynamicExpansion(3, 20, 2, 5, 1, new Date())).isSuccess());
        // error situation: dynamic-expansion not exist
        Assertions.assertFalse(groupServiceImpl.updateDynamicExpansionStatus(new PluginDynamicExpansion(1, 8, 2, 5, 1
            , new Date())).isSuccess());
        // error situation: k8s error
        when(k8scliFeign.createAutoAdjustReplica(any())).thenThrow(new RuntimeException("junit-test"));
        when(k8scliFeign.deleteAutoAdjustReplica(anyString())).thenThrow(new RuntimeException("junit-test"));
        Assertions.assertFalse(groupServiceImpl.
            updateDynamicExpansionStatus(new PluginDynamicExpansion(3, 8, 2, 5, 1, new Date())).isSuccess());
    }
}