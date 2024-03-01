package com.iscas.apiservice.mapper;

import com.iscas.apiservice.pojo.Parameter;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Service
public interface ParamMapper {
    @Insert("INSERT INTO Params (param_id,api_id,name,type,title) VALUES (#{paramId},#{apiId},#{name},#{type},"
        + "#{title})")
    @Options(useGeneratedKeys = true, keyProperty = "paramId", keyColumn = "paramId")
    int addParam(Parameter parameter);

    @Insert({"<script>",
        "INSERT INTO Params (param_id, api_id, name, type, title)",
        "VALUES",
        "<foreach item='param' collection='params' separator=','>",
        "(#{param.paramId}, #{param.apiId}, #{param.name}, #{param.type}, #{param.title})",
        "</foreach>",
        "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "paramId", keyColumn = "paramId")
    int addParams(@Param("params") List<Parameter> params);

    @Select("SELECT * FROM Params WHERE api_id = #{apiId}")
    ArrayList<Parameter> getParamsByApiId(int apiId);

    @Update("UPDATE Params SET group_id = #{groupId}, src_id = #{srcId}, name = #{name}, api_desc = #{apiDesc}, "
        + "code = #{code}, url = #{url} WHERE param_id = #{paramId} ")
    int updateParam(Parameter parameter);

    @Delete("DELETE FROM Params WHERE api_id = #{apiId}")
    int deleteParamsOfApi(int apiId);
}
