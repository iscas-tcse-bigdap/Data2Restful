package com.iscas.apiservice.mapper;

import com.iscas.apiservice.pojo.PluginAcl;
import com.iscas.apiservice.pojo.PluginAclDetail;
import com.iscas.apiservice.pojo.PluginDynamicExpansion;
import com.iscas.apiservice.pojo.PluginDynamicRateLimit;
import com.iscas.apiservice.pojo.PluginKeyAuth;
import com.iscas.apiservice.pojo.controllerToWeb.AclUserIdName;
import com.iscas.apiservice.pojo.dbTemplate.CreateAclDetailItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author wbq
 * @version 1.0
 * @title PluginMapper
 * @description
 * @create 2023/10/25 14:06
 */

@Mapper
@Service
public interface PluginMapper {

    @Select("select * from plugin_key_auth where group_id = #{groupId}")
    PluginKeyAuth getPluginKeyAuth(int groupId);

    @Update("update plugin_key_auth set key_name = #{keyName}, key_pwd = #{keyPwd}, update_time = #{updateTime} where"
        + " group_id = #{groupId}")
    int updateKeyAuthPlugin(PluginKeyAuth pluginKeyAuth);

    @Update("update plugin_key_auth set status = #{status} where key_auth_id = #{keyAuthId}")
    int updateKeyAuthStatus(int keyAuthId, int status);

    @Delete("delete from plugin_key_auth where group_id = #{groupId}")
    int deleteKeyAuthPlugin(int groupId);

    @Insert("insert into plugin_key_auth (key_name, key_pwd, group_id, status, update_time) "
        + "values (#{keyName},#{keyPwd},#{groupId},#{status},#{updateTime})")
    int addKeyAuthPlugin(PluginKeyAuth pluginKeyAuth);

    @Select("select pad.*, user.username from plugin_acl_detail pad left join user on  pad.acl_user_id = user.user_id "
        + "where pad.acl_id = #{aclId} and pad.strategy = 1")
    List<PluginAclDetail> getAclWhiteList(int aclId);

    @Select("select user.user_id, user.username as value from plugin_acl_detail pad "
        + "left join user on pad.acl_user_id = user.user_id where pad.acl_id = #{aclId} and pad.strategy = 1")
    List<AclUserIdName> getAclWhiteListOutline(int aclId);

    @Select("select pad.*, user.username from plugin_acl_detail pad left join user on pad.acl_user_id = user.user_id "
        + "where pad.acl_id = #{aclId} and pad.strategy = 0")
    List<PluginAclDetail> getAclBlackList(int aclId);

    @Select("select user.user_id, user.username as value from plugin_acl_detail pad "
        + "left join user on pad.acl_user_id = user.user_id where pad.acl_id = #{aclId} and pad.strategy = 0")
    List<AclUserIdName> getAclBlackListOutline(int aclId);

    @Select("select * from plugin_acl where group_id = #{groupId}")
    PluginAcl getPluginAcl(int groupId);

    @Select("select * from plugin_acl where acl_id = #{aclId}")
    PluginAcl getPluginAclById(int aclId);

    @Select("select * from plugin_dynamic_rate_limit where group_id = #{groupId}")
    PluginDynamicRateLimit getPluginDynamicRateLimit(int groupId);

    @Select("select * from plugin_dynamic_expansion where group_id = #{groupId}")
    PluginDynamicExpansion getPluginDynamicExpansion(int groupId);

    @Insert("insert into plugin_acl (group_id) values (#{groupId})")
    void createAclPlugin(PluginAcl pluginAcl);

    @Insert({
        "<script>",
        "INSERT INTO plugin_acl_detail (acl_user_id, acl_id, strategy) VALUES ",
        "<foreach collection='list' item='item' index='index' separator=','>",
        "(#{item.aclUserId}, #{item.aclId}, 1)",
        "</foreach>",
        "</script>"
    })
    void createNewWhitelistDetail(List<CreateAclDetailItem> whiteList);

    @Insert({
        "<script>",
        "INSERT INTO plugin_acl_detail (acl_user_id, acl_id, strategy) VALUES ",
        "<foreach collection='list' item='item' index='index' separator=','>",
        "(#{item.aclUserId}, #{item.aclId}, 0)",
        "</foreach>",
        "</script>"
    })
    void createNewBlacklistDetail(List<CreateAclDetailItem> blacklist);

    @Update("update plugin_acl set update_time = #{date} where acl_id = #{aclId}")
    void updateAclUpdateTime(Date date, int aclId);

    @Update("UPDATE plugin_acl SET status = #{status} WHERE acl_id = #{aclId}")
    int updateAclStatus(int aclId, int status);

    @Delete("delete from plugin_acl where group_id = #{groupId}")
    void deleteAclPlugin(int groupId);

    @Delete("delete from plugin_acl_detail where acl_id = #{aclId}")
    void deleteAclPluginDetail(int aclId);

    @Insert("insert into plugin_dynamic_rate_limit (group_id) values (#{groupId})")
    void createRateLimitPlugin(PluginDynamicRateLimit pluginDynamicRateLimit);

    @Delete("delete from plugin_dynamic_rate_limit where group_id = #{groupId}")
    void deleteRateLimitPlugin(int groupId);

    @Update("UPDATE plugin_dynamic_rate_limit SET rate_limit = #{rateLimit}, time_window = #{timeWindow}, "
        + "update_time = #{updateTime} WHERE group_id = #{groupId}")
    int updateRateLimitPlugin(PluginDynamicRateLimit pluginDynamicRateLimit);

    @Update("UPDATE plugin_dynamic_rate_limit SET status = #{status} WHERE rate_limit_id = #{rateLimitId}")
    int updateRateLimitStatus(int rateLimitId, int status);

    @Insert("insert into plugin_dynamic_expansion (group_id) values (#{groupId})")
    void createDynamicExpansionPlugin(PluginDynamicExpansion pluginDynamicExpansion);

    @Delete("delete from plugin_dynamic_expansion where group_id = #{groupId}")
    void deleteDynamicExpansionPlugin(int groupId);

    @Update("UPDATE plugin_dynamic_expansion SET min_value = #{minValue}, max_value = #{maxValue}, "
        + "update_time = #{updateTime} WHERE group_id = #{groupId}")
    int updateDynamicExpansionPlugin(PluginDynamicExpansion pluginDynamicExpansion);

    @Update("UPDATE plugin_dynamic_expansion SET status = #{status} WHERE dynamic_expansion_id = #{dynamicExpansionId}")
    int updateDynamicExpansionStatus(int dynamicExpansionId, int status);

    @Select("select status from plugin_dynamic_expansion where group_id = #{groupId}")
    int getDynamicExpansionStatus(int groupId);

    @Select("select min_value, max_value from plugin_dynamic_expansion where group_id = #{groupId}")
    PluginDynamicExpansion getDynamicExpansionValue(int groupId);

    @Select("select * from plugin_dynamic_expansion where dynamic_expansion_id = #{dynamicExpansionId}")
    PluginDynamicExpansion getDynamicExpansion(int dynamicExpansionId);
}
