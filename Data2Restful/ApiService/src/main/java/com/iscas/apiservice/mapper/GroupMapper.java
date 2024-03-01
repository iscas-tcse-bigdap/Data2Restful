package com.iscas.apiservice.mapper;

import com.iscas.apiservice.pojo.Group;
import com.iscas.apiservice.pojo.dbTemplate.PluginStatus;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Mapper
@Service
public interface GroupMapper {
    @Insert("INSERT INTO Apigroup (group_id,src_id,name,group_desc,create_time) "
        + "VALUES (#{groupId},#{srcId},#{name},#{groupDesc},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "groupId")
    int createGroup(Group group);

    @Select("SELECT * FROM Apigroup")
    ArrayList<Group> getGroupList();

    @Select("SELECT name FROM Apigroup WHERE group_id = #{groupId}")
    String getGroupNameById(int groupId);

    @Delete("DELETE FROM Apigroup WHERE group_id = #{groupId}")
    int deleteGroup(int groupId);

    @Update("UPDATE Apigroup SET name = #{name}, group_desc = #{groupDesc}, src_id = #{srcId} "
        + "WHERE group_id = #{groupId} ")
    int updateGroup(Group group);

    @Select("SELECT * FROM Apigroup WHERE group_id = #{groupId}")
    Group getGroup(int groupId);

    @Select("select * from apigroup where name = #{groupName}")
    Group getGroupByName(String groupName);

    @Select("select key_auth_status, acl_status, rate_limit_status, dynamic_expansion_status from apigroup "
        + "where group_id = #{groupId}")
    PluginStatus getPlugin(int groupId);

    @Update("UPDATE Apigroup SET key_auth_status = 0 WHERE group_id = #{groupId}")
    void removeKeyAuthPlugin(int groupId);

    @Update("UPDATE Apigroup SET key_auth_status = 1 WHERE group_id = #{groupId}")
    void setupKeyAuthPlugin(int groupId);

    @Update("UPDATE Apigroup SET acl_status = 1 WHERE group_id = #{groupId}")
    void setupAclPlugin(int groupId);

    @Update("UPDATE Apigroup SET rate_limit_status = 1 WHERE group_id = #{groupId}")
    void setupRateLimitPlugin(int groupId);

    @Update("UPDATE Apigroup SET dynamic_expansion_status = 1 WHERE group_id = #{groupId}")
    void setupDynamicExpansionPlugin(int groupId);

    @Update("update Apigroup set acl_status = 0 where group_id = #{groupId}")
    void removeAclPlugin(int groupId);

    @Update("update Apigroup set rate_limit_status = 0 where group_id = #{groupId}")
    void removeRateLimitPlugin(int groupId);

    @Update("update Apigroup set dynamic_expansion_status = 0 where group_id = #{groupId}")
    void removeDynamicExpansionPlugin(int groupId);
}
