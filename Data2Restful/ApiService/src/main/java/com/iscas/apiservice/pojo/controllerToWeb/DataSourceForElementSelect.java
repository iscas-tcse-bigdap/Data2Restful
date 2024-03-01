package com.iscas.apiservice.pojo.controllerToWeb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iscas.apiservice.pojo.DataSource;
import lombok.Data;

/**
 * @author wbq
 * @version 1.0
 * @title DataSourceForElementSelect
 * @description
 * @create 2023/12/6 14:10
 */

@Data
public class DataSourceForElementSelect {
    @JsonProperty(value = "srcId")
    @TableId(value = "src_id", type = IdType.AUTO)
    private int srcId;

    private String name;
    private String srcType;
    private String driverClassName;
    private String host;
    private String port;
    private String db;
    private String username;
    private String password;
    private String rmark;
    private int status;

    private String label;
    private String value;

    public DataSourceForElementSelect(DataSource dataSource) {
        this.srcId = dataSource.getSrcId();
        this.name = dataSource.getName();
        this.srcType = dataSource.getSrcType();
        this.driverClassName = dataSource.getDriverClassName();
        this.host = dataSource.getHost();
        this.port = dataSource.getPort();
        this.db = dataSource.getDb();
        this.username = dataSource.getUsername();
        this.password = dataSource.getPassword();
        this.rmark = dataSource.getRmark();
        this.status = dataSource.getStatus();
        this.label = dataSource.getName();
        this.value = dataSource.getName();
    }
}
