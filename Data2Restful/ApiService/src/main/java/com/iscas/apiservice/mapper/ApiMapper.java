package com.iscas.apiservice.mapper;

import com.iscas.apiservice.pojo.Api;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Service
public interface ApiMapper {

    //    @Insert("INSERT INTO Api (api_id,group_id,src_id,name,api_desc,code,url) VALUES (#{apiId},#{groupId},
    //    #{srcId},#{name},#{apiDesc},#{code},#{url})")
//    @Options(useGeneratedKeys=true, keyProperty="apiId", keyColumn="apiId")
    void createApi(Api api);

    @Select("SELECT * FROM Api")
    ArrayList<Api> getApiList();

    @Select("SELECT * FROM Api WHERE api_id = #{apiId}")
    Api getApiInfoByApiId(int apiId);

    // 这种方式无法自定义返回结构，没必要
//    @Select("SELECT * FROM Api join apigroup on api.group_id = apigroup.group_id join datasource on api.src_id =
//    datasource.src_id WHERE api_id = #{apiId}")
//    ApiAllDetail getApiDetailByApiId(int apiId);

    @Select("SELECT * FROM Api WHERE group_id = #{groupId}")
    ArrayList<Api> getApiListByGroupId(int groupId);

    @Select("SELECT code FROM Api WHERE api_id = #{apiId}")
    String getCodeByApiId(int apiId);

    @Update("UPDATE Api SET group_id = #{groupId}, src_id = #{srcId}, name = #{name}, api_desc = #{apiDesc}, code = "
        + "#{code}, url = #{url}, ins_num = #{insNum}, initInstanceNum = #{initInstanceNum}, status = #{status}, "
        + "is_public = #{isPublic} WHERE api_id = #{apiId} ")
    int updateApi(Api api);

    @Delete("DELETE FROM Api WHERE api_id = #{apiId}")
    int deleteApi(int apiId);

    @Delete("delete from api where group_id = #{groupId}")
    void deleteApiByGroupId(int groupId);

    @Update("UPDATE Api SET status = 1 WHERE api_id = #{apiId}")
    int publishApi(int apiId);

    @Update("UPDATE Api SET status = 0 WHERE api_id = #{apiId}")
    int terminateApi(int apiId);

    @Select("SELECT * FROM Api WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    ArrayList<Api> getApiListByKeyword(String keyword);

    @Select("SELECT status FROM api WHERE group_id = #{groupId} and status = 1")
    List<Integer> getRunningSvc(int groupId);
}

