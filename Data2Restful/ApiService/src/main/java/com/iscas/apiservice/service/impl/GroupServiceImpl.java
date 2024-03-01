package com.iscas.apiservice.service.impl;

import com.iscas.apiservice.feign.K8scliFeign;
import com.iscas.apiservice.mapper.ApiMapper;
import com.iscas.apiservice.mapper.GroupMapper;
import com.iscas.apiservice.mapper.PluginMapper;
import com.iscas.apiservice.pojo.Group;
import com.iscas.apiservice.pojo.PluginAcl;
import com.iscas.apiservice.pojo.PluginDynamicExpansion;
import com.iscas.apiservice.pojo.PluginDynamicRateLimit;
import com.iscas.apiservice.pojo.PluginKeyAuth;
import com.iscas.apiservice.pojo.controllerToWeb.Acl;
import com.iscas.apiservice.pojo.controllerToWeb.PluginDetail;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.AclConfig;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.DynamicExpansionConfig;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.KeyAuthConfig;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.PluginAllocateOutline;
import com.iscas.apiservice.pojo.controllerToWeb.plugin.RateLimitConfig;
import com.iscas.apiservice.pojo.dbTemplate.CreateAcl;
import com.iscas.apiservice.pojo.dbTemplate.CreateAclDetailItem;
import com.iscas.apiservice.pojo.dbTemplate.PluginStatus;
import com.iscas.apiservice.pojo.feignToK8scli.CreateHpaInfo;
import com.iscas.apiservice.pojo.feignToK8scli.CreateIngressInfo;
import com.iscas.apiservice.service.GroupService;
import com.iscas.apiservice.utils.BeanUtil;
import com.iscas.apiservice.utils.Response;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupMapper groupMapper;

    @Setter
    @Autowired
    private K8scliFeign k8scliFeign;

    @Autowired
    private PluginMapper pluginMapper;

    @Autowired
    private ApiMapper apiMapper;

    private static final int ERROR_CODE1 = -1;
    private static final int ERROR_CODE2 = -2;

    /**
     * 创建数据服务组.
     */
    @Override
    @Transactional
    public Integer createGroup(Group group) {
        Date currentDate = new Date(); // 获取当前时间
        group.setCreateTime(currentDate);
        try {
            groupMapper.createGroup(group);
        } catch (Exception e) {
            System.out.println("创建服务分组时发生错误：" + e.getMessage());
            return ERROR_CODE1;
        }
        CreateIngressInfo createIngressInfo = new CreateIngressInfo();
        createIngressInfo.setGroupName(group.getName());
        try {
            k8scliFeign.createIngress(createIngressInfo);
        } catch (Exception e) {
            System.out.println("创建ingress时发生错误：" + e.getMessage());
            // transaction rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ERROR_CODE2;
        }
        return group.getGroupId();
    }

    /**
     * 数据服务组列表.
     */
    @Override
    public ArrayList<Group> getGroupList() {
        return groupMapper.getGroupList();
    }

    /**
     * 数据服务组详情.
     */
    @Override
    public Group getGroup(int groupId) {
        return groupMapper.getGroup(groupId);
    }

    /**
     * 通过组名称获取数据服务组详情.
     */
    @Override
    public Group getGroupByName(String groupName) {
        return groupMapper.getGroupByName(groupName);
    }

    /**
     * 删除数据服务组.
     */
    @Override
    @Transactional
    public Response deleteGroup(int groupId, String groupName) {
        if (groupMapper.getGroup(groupId) == null) {
            return Response.error().message("服务分组不存在");
        }
        int groupDynamicExpansionStatus = groupMapper.getPlugin(groupId).getDynamicExpansionStatus();
        ApiServiceImpl apiServiceBean = BeanUtil.getBean(ApiServiceImpl.class);
        apiMapper.getApiListByGroupId(groupId).forEach(api -> apiServiceBean.deleteApiAndParams(api.getApiId()));
        groupMapper.deleteGroup(groupId);
        try {
            if (groupDynamicExpansionStatus == 1 && pluginMapper.getDynamicExpansionStatus(groupId) == 1) {
                k8scliFeign.deleteAutoAdjustReplica(groupName);
            }
            k8scliFeign.deleteIngress(groupName);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.error().message("删除ingress时发生错误");
        }
        return Response.ok().message("删除服务分组成功");
    }

    /**
     * 更新数据服务组.
     */
    @Override
    public int updateGroup(Group group) {
        return groupMapper.updateGroup(group);
    }

    /**
     * 获取已分配插件列表.
     */
    @Override
    public Response getPluginList(int groupId) {
        PluginStatus pluginStatus = groupMapper.getPlugin(groupId);
        if (pluginStatus == null) {
            return Response.error().message("服务分组不存在或尚未分配插件");
        }
        List<PluginDetail> pluginList = new ArrayList<>();
        if (pluginStatus.getKeyAuthStatus() == 1) {
            PluginKeyAuth pluginKeyAuth = pluginMapper.getPluginKeyAuth(groupId);
            PluginDetail pluginDetail = new PluginDetail(pluginKeyAuth, new KeyAuthConfig(pluginKeyAuth));
            pluginList.add(pluginDetail);
        }
        if (pluginStatus.getAclStatus() == 1) {
            PluginAcl acl = pluginMapper.getPluginAcl(groupId);
            if (acl == null) {
                return Response.error().message("系统异常：该服务分组未配置ACL");
            }
            AclConfig aclConfig = new AclConfig(pluginMapper.getAclWhiteListOutline(acl.getAclId()),
                pluginMapper.getAclBlackListOutline(acl.getAclId()));
            PluginDetail pluginDetail = new PluginDetail(acl, aclConfig);
            pluginList.add(pluginDetail);
        }
        if (pluginStatus.getRateLimitStatus() == 1) {
            PluginDynamicRateLimit rateLimit = pluginMapper.getPluginDynamicRateLimit(groupId);
            PluginDetail pluginDetail = new PluginDetail(rateLimit, new RateLimitConfig(rateLimit));
            pluginList.add(pluginDetail);
        }
        if (pluginStatus.getDynamicExpansionStatus() == 1) {
            PluginDynamicExpansion pluginDynamicExpansion = pluginMapper.getPluginDynamicExpansion(groupId);
            PluginDetail pluginDetail = new PluginDetail(pluginDynamicExpansion,
                new DynamicExpansionConfig(pluginDynamicExpansion));
            pluginList.add(pluginDetail);
        }
        return Response.ok().data("pluginList", pluginList);
    }

    /**
     * 获取未分配插件列表.
     */
    @Override
    public Response getUnallocatedPluginList(int groupId) {
        PluginStatus pluginStatus = groupMapper.getPlugin(groupId);
        if (pluginStatus == null) {
            return Response.error().message("服务分组不存在或尚未分配插件");
        }
        List<PluginAllocateOutline> pluginAllocateOutlineList = new ArrayList<>();
        if (pluginStatus.getKeyAuthStatus() == 0) {
            pluginAllocateOutlineList.add(new PluginAllocateOutline("key-auth", "配置该插件后，访问服务内接口需要预先配置的凭据"));
        }
        if (pluginStatus.getAclStatus() == 0) {
            pluginAllocateOutlineList.add(new PluginAllocateOutline("acl", "配置该插件后，可以配置当前数据服务的黑/白名单"));
        }
        if (pluginStatus.getRateLimitStatus() == 0) {
            pluginAllocateOutlineList.add(new PluginAllocateOutline("rate-limit", "配置该插件后，可以配置当前数据服务的限流策略"));
        }
        if (pluginStatus.getDynamicExpansionStatus() == 0) {
            pluginAllocateOutlineList.add(new PluginAllocateOutline("dynamic-expansion", "配置该插件后，可以配置当前数据服务的动态扩容策略"));
        }
        return Response.ok().data("pluginAllocateOutlineList", pluginAllocateOutlineList);
    }

    /**
     * 更新key-auth插件.
     */
    @Override
    public Response updateKeyAuthPlugin(PluginKeyAuth pluginKeyAuth) throws NoSuchAlgorithmException {
        if (pluginKeyAuth.getKeyPwd() == null) {
            return Response.error().message("KeyAuth插件密码不能为空");
        }
        pluginKeyAuth.setKeyPwd(encode(pluginKeyAuth.getKeyPwd()));
        pluginKeyAuth.setUpdateTime(new Date());
        int result = pluginMapper.updateKeyAuthPlugin(pluginKeyAuth);
        return result == 1 ? Response.ok().message("KeyAuth插件更新成功") : Response.error().message("KeyAuth插件更新失败");
    }

    /**
     * key-auth加密.
     */
    private static String encode(String input) throws NoSuchAlgorithmException {
        // 单向不可逆加密
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digest1 = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest1);
    }

    /**
     * 更新key-auth插件状态.
     */
    @Override
    public Response updateKeyAuthStatus(PluginKeyAuth pluginKeyAuth) {
        int result = pluginMapper.updateKeyAuthStatus(pluginKeyAuth.getKeyAuthId(), pluginKeyAuth.getStatus());
        return result == 1 ? Response.ok().message("KeyAuth状态更新成功") : Response.error().message("KeyAuth状态更新失败");
    }

    /**
     * 删除key-auth插件.
     */
    @Override
    @Transactional
    public Response deleteKeyAuthPlugin(int groupId) {
        // 删除和更新都不会引起报错，因此无需try-catch
        pluginMapper.deleteKeyAuthPlugin(groupId);
        groupMapper.removeKeyAuthPlugin(groupId);
        return Response.ok().message("KeyAuth插件删除成功");
    }

    /**
     * 添加key-auth插件状态.
     */
    @Override
    @Transactional
    public Response addKeyAuthPlugin(PluginKeyAuth pluginKeyAuth) {
        if (groupMapper.getGroup(pluginKeyAuth.getGroupId()) == null) {
            return Response.error().message("服务分组不存在");
        }
        if (groupMapper.getPlugin(pluginKeyAuth.getGroupId()).getKeyAuthStatus() == 1) {
            return Response.error().message("KeyAuth插件添加失败，因为该服务分组已经存在KeyAuth插件");
        }
        pluginKeyAuth.setDefaultValue("key_auth", "123", 0);
        pluginMapper.addKeyAuthPlugin(pluginKeyAuth);
        groupMapper.setupKeyAuthPlugin(pluginKeyAuth.getGroupId());
        return Response.ok().message("KeyAuth插件添加成功");
    }

    /**
     * 更新eky-auth插件状态.
     */
    @Override
    public Response getKeyAuth(int groupId) {
        return Response.ok().data("keyAuth", pluginMapper.getPluginKeyAuth(groupId));
    }

    /**
     * 添加key-auth插件.
     */
    @Override
    public Response getAcl(int groupId) {
        PluginAcl pluginAcl = pluginMapper.getPluginAcl(groupId);
        return pluginAcl == null ? Response.ok().data("acl", new Acl())
            : Response.ok().data("acl", new Acl(pluginMapper.getAclWhiteList(pluginAcl.getAclId()),
                pluginMapper.getAclBlackList(pluginAcl.getAclId()))).data("aclStatus", pluginAcl.getStatus());
    }

    /**
     * 添加ACL插件.
     */
    @Override
    @Transactional
    public Response createAclPlugin(PluginAcl pluginAcl) {
        if (groupMapper.getPlugin(pluginAcl.getGroupId()) == null) {
            return Response.error().message("服务分组不存在");
        }
        if (groupMapper.getPlugin(pluginAcl.getGroupId()).getAclStatus() == 1) {
            return Response.error().message("Acl插件添加失败，因为该服务分组已经存在Acl插件");
        }
        pluginMapper.createAclPlugin(pluginAcl);
        groupMapper.setupAclPlugin(pluginAcl.getGroupId());
        return Response.ok().message("Acl插件添加成功");
    }

    /**
     * 删除ACL插件.
     */
    @Override
    @Transactional
    public Response deleteAclPlugin(int groupId) {
        try {
            pluginMapper.deleteAclPluginDetail(pluginMapper.getPluginAcl(groupId).getAclId());
            pluginMapper.deleteAclPlugin(groupId);
            groupMapper.removeAclPlugin(groupId);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.error().message("Acl插件删除失败");
        }
        return Response.ok().message("Acl插件删除成功");
    }

    /**
     * 更新ACL插件.
     */
    @Override
    @Transactional
    public Response updateAclPlugin(CreateAcl acl) {
        if (pluginMapper.getPluginAclById(acl.getAclId()) == null) {
            return Response.error().message("Acl插件不存在");
        }
        pluginMapper.deleteAclPluginDetail(acl.getAclId());
        try {
            if (!acl.getWhitelist().isEmpty()) {
                for (CreateAclDetailItem item : acl.getWhitelist()) {
                    if (item.getAclId() != acl.getAclId()) {
                        throw new Exception("白名单内容不合法");
                    }
                }
                pluginMapper.createNewWhitelistDetail(acl.getWhitelist());
            }
            if (!acl.getBlacklist().isEmpty()) {
                for (CreateAclDetailItem item : acl.getBlacklist()) {
                    if (item.getAclId() != acl.getAclId()) {
                        throw new Exception("黑名单内容不合法");
                    }
                }
                pluginMapper.createNewBlacklistDetail(acl.getBlacklist());
            }
            pluginMapper.updateAclUpdateTime(new Date(), acl.getAclId());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.error().message("Acl新内容插入失败");
        }
        return Response.ok().message("Acl插件更新成功");
    }

    /**
     * 更新ACL插件状态.
     */
    @Override
    public Response updateAclStatus(PluginAcl acl) {
        int result = pluginMapper.updateAclStatus(acl.getAclId(), acl.getStatus());
        return result == 1 ? Response.ok().message("Acl状态更新成功") : Response.error().message("Acl状态更新失败");
    }

    /**
     * 添加RateLimit插件.
     */
    @Override
    @Transactional
    public Response createRateLimitPlugin(PluginDynamicRateLimit pluginDynamicRateLimit) {
        if (groupMapper.getGroup(pluginDynamicRateLimit.getGroupId()) == null) {
            return Response.error().message("服务分组不存在");
        }
        if (groupMapper.getPlugin(pluginDynamicRateLimit.getGroupId()).getRateLimitStatus() == 1) {
            return Response.error().message("RateLimit插件添加失败，因为该服务分组已经存在RateLimit插件");
        }
        pluginMapper.createRateLimitPlugin(pluginDynamicRateLimit);
        groupMapper.setupRateLimitPlugin(pluginDynamicRateLimit.getGroupId());
        return Response.ok().message("RateLimit插件添加成功");
    }

    /**
     * 删除RateLimit插件.
     */
    @Override
    @Transactional
    public Response deleteRateLimitPlugin(int groupId) {
        if (groupMapper.getGroup(groupId) == null) {
            return Response.error().message("服务分组不存在");
        }
        if (groupMapper.getPlugin(groupId).getAclStatus() == 0) {
            return Response.error().message("rateLimit插件删除失败，因为该服务分组不存在rateLimit插件");
        }
        pluginMapper.deleteRateLimitPlugin(groupId);
        groupMapper.removeRateLimitPlugin(groupId);
        return Response.ok().message("RateLimit插件删除成功");
    }

    /**
     * 更新RateLimit插件.
     */
    @Override
    public Response updateRateLimitPlugin(PluginDynamicRateLimit pluginDynamicRateLimit) {
        pluginDynamicRateLimit.setUpdateTime(new Date());
        return pluginMapper.updateRateLimitPlugin(pluginDynamicRateLimit) == 1
            ? Response.ok().message("RateLimit插件更新成功") : Response.error().message("RateLimit插件更新失败");
    }

    /**
     * 更新RateLimit插件状态.
     */
    @Override
    public Response updateRateLimitStatus(PluginDynamicRateLimit pluginDynamicRateLimit) {
        int result = pluginMapper.updateRateLimitStatus(pluginDynamicRateLimit.getRateLimitId(),
            pluginDynamicRateLimit.getStatus());
        return result == 1 ? Response.ok().message("RateLimit状态更新成功") : Response.error().message("RateLimit状态更新失败");
    }

    /**
     * 添加DynamicExpansion插件.
     */
    @Override
    @Transactional
    public Response createDynamicExpansionPlugin(PluginDynamicExpansion pluginDynamicExpansion) {
        if (groupMapper.getGroup(pluginDynamicExpansion.getGroupId()) == null) {
            return Response.error().message("服务分组不存在");
        }
        if (groupMapper.getPlugin(pluginDynamicExpansion.getGroupId()).getDynamicExpansionStatus() == 1) {
            return Response.error().message("DynamicExpansion插件添加失败，因为该服务分组已经存在DynamicExpansion插件");
        }
        pluginDynamicExpansion.setUpdateTime(new Date());
        pluginMapper.createDynamicExpansionPlugin(pluginDynamicExpansion);
        groupMapper.setupDynamicExpansionPlugin(pluginDynamicExpansion.getGroupId());
        return Response.ok().message("DynamicExpansion插件添加成功");
    }

    /**
     * 删除DynamicExpansion插件.
     */
    @Override
    @Transactional
    public Response deleteDynamicExpansionPlugin(int groupId) {
        if (groupMapper.getGroup(groupId) == null) {
            return Response.error().message("服务分组不存在");
        }
        if (groupMapper.getPlugin(groupId).getDynamicExpansionStatus() == 0) {
            return Response.error().message("DynamicExpansion插件删除失败，因为该服务分组不存在DynamicExpansion插件");
        }
        try {
            if (pluginMapper.getDynamicExpansionStatus(groupId) == 1) {
                k8scliFeign.deleteAutoAdjustReplica(groupMapper.getGroupNameById(groupId));
            }
            pluginMapper.deleteDynamicExpansionPlugin(groupId);
            groupMapper.removeDynamicExpansionPlugin(groupId);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.error().message("DynamicExpansion插件删除失败");
        }
        return Response.ok().message("DynamicExpansion插件删除成功");
    }

    /**
     * 更新DynamicExpansion插件.
     */
    @Override
    @Transactional
    public Response updateDynamicExpansionPlugin(PluginDynamicExpansion pluginDynamicExpansion) {
        pluginDynamicExpansion.setUpdateTime(new Date());
        int result = pluginMapper.updateDynamicExpansionPlugin(pluginDynamicExpansion);
        if (result == 1 && pluginMapper.getDynamicExpansionStatus(pluginDynamicExpansion.getGroupId()) == 1) {
            try {
                CreateHpaInfo createHpaInfo =
                    new CreateHpaInfo(groupMapper.getGroupNameById(pluginDynamicExpansion.getGroupId()),
                        pluginDynamicExpansion.getMinValue(), pluginDynamicExpansion.getMaxValue());
                k8scliFeign.updateAutoAdjustReplicaNum(createHpaInfo);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Response.error().message("集群动态扩展配置异常");
            }
        }
        return result == 1 ? Response.ok().message("DynamicExpansion插件更新成功") : Response.error().message(
            "DynamicExpansion插件更新失败");
    }

    /**
     * 更新DynamicExpansion插件状态.
     */
    @Override
    @Transactional
    public Response updateDynamicExpansionStatus(PluginDynamicExpansion pluginDynamicExpansion) {
        if (groupMapper.getGroup(pluginDynamicExpansion.getGroupId()) == null
            || groupMapper.getPlugin(pluginDynamicExpansion.getGroupId()).getDynamicExpansionStatus() == 0) {
            return Response.error().message("分组不存在或未开启dynamicExpansion");
        }
        if (pluginMapper.getDynamicExpansion(pluginDynamicExpansion.getDynamicExpansionId()) == null) {
            return Response.error().message("pluginDynamicExpansion不存在");
        }
        pluginMapper.updateDynamicExpansionStatus(pluginDynamicExpansion.getDynamicExpansionId(),
            pluginDynamicExpansion.getStatus());
        try {
            if (pluginDynamicExpansion.getStatus() == 1) {
                PluginDynamicExpansion value =
                    pluginMapper.getDynamicExpansionValue(pluginDynamicExpansion.getGroupId());
                CreateHpaInfo createHpaInfo =
                    new CreateHpaInfo(groupMapper.getGroupNameById(pluginDynamicExpansion.getGroupId()),
                        value.getMinValue(), value.getMaxValue());
                k8scliFeign.createAutoAdjustReplica(createHpaInfo);
            } else {
                k8scliFeign.deleteAutoAdjustReplica(groupMapper.getGroupNameById(pluginDynamicExpansion.getGroupId()));
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.error().message("集群动态扩展配置异常，请确保当前服务已有发布的接口");
        }
        return Response.ok().message("DynamicExpansion状态更新成功");
    }
}
