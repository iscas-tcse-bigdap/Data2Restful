package com.iscas.apiservice.service;

import com.iscas.apiservice.pojo.Group;
import com.iscas.apiservice.pojo.PluginAcl;
import com.iscas.apiservice.pojo.PluginDynamicExpansion;
import com.iscas.apiservice.pojo.PluginDynamicRateLimit;
import com.iscas.apiservice.pojo.PluginKeyAuth;
import com.iscas.apiservice.pojo.dbTemplate.CreateAcl;
import com.iscas.apiservice.utils.Response;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface GroupService {
    Integer createGroup(Group group);

    ArrayList<Group> getGroupList();

    Response deleteGroup(int groupId, String groupName);

    int updateGroup(Group group);

    Group getGroup(int groupId);

    Group getGroupByName(String groupName);

    Response getPluginList(int groupId);

    Response getUnallocatedPluginList(int groupId);

    Response updateKeyAuthPlugin(PluginKeyAuth pluginKeyAuth) throws NoSuchAlgorithmException;

    Response updateKeyAuthStatus(PluginKeyAuth pluginKeyAuth);

    Response deleteKeyAuthPlugin(int groupId);

    Response addKeyAuthPlugin(PluginKeyAuth pluginKeyAuth) throws NoSuchAlgorithmException;

    Response getKeyAuth(int groupId);

    Response getAcl(int groupId);

    Response createAclPlugin(PluginAcl pluginKeyAuth);

    Response deleteAclPlugin(int groupId);

    Response updateAclPlugin(CreateAcl acl);

    Response updateAclStatus(PluginAcl acl);

    Response createRateLimitPlugin(PluginDynamicRateLimit pluginDynamicRateLimit);

    Response deleteRateLimitPlugin(int groupId);

    Response updateRateLimitPlugin(PluginDynamicRateLimit pluginDynamicRateLimit);

    Response updateRateLimitStatus(PluginDynamicRateLimit pluginDynamicRateLimit);

    Response createDynamicExpansionPlugin(PluginDynamicExpansion pluginDynamicExpansion);

    Response deleteDynamicExpansionPlugin(int groupId);

    Response updateDynamicExpansionPlugin(PluginDynamicExpansion pluginDynamicExpansion);

    Response updateDynamicExpansionStatus(PluginDynamicExpansion pluginDynamicExpansion);
}
