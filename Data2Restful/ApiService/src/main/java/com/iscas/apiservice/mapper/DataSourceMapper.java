package com.iscas.apiservice.mapper;

import com.iscas.apiservice.pojo.DataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 *@title DataSourceMapper
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/9/13 16:22
 */
@Mapper
@Service
public interface DataSourceMapper {
    @Select("SELECT * FROM Datasource")
    ArrayList<DataSource> getDataSourceList();

    @Select("SELECT * FROM Datasource WHERE src_id = #{srcId}")
    DataSource getDataSourceByID(int srcId);

    @Insert("INSERT INTO Datasource (src_id,name,src_type,driver_class_name,host,port,db,username,password,rmark) "
        + "VALUES (#{srcId},#{name},#{srcType},#{driverClassName},#{host},#{port},#{db},#{username},#{password},"
        + "#{rmark})")
    @Options(useGeneratedKeys = true, keyProperty = "srcId", keyColumn = "srcId")
    void createDataSource(DataSource dataSource);

    @Delete("DELETE FROM Datasource WHERE src_id = #{srcId}")
    int deleteDataSource(int srcId);

    @Update("UPDATE Datasource SET src_id = #{srcId}, name = #{name}, src_type = #{srcType}, driver_class_name = "
        + "#{driverClassName}, host = #{host}, port = #{port}, db = #{db}, username = #{username}, password = "
        + "#{password}, rmark = #{rmark} WHERE src_id = #{srcId} ")
    int updateDataSource(DataSource dataSource);
}
