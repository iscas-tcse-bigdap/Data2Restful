package com.iscas.apiservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wbq
 * @version 1.0
 * @title DataSource
 * @description
 * @create 2023/9/13 16:23
 */
@Data
@NoArgsConstructor
public class DataSource {
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

    public DataSource(int newSrcId, String newName, String newSrcType, String newDriverClassName, String newHost,
                      String newPort, String newDb, String newUsername, String newPwd, String newRmark, int newStatus) {
        this.srcId = newSrcId;
        this.name = newName;
        this.srcType = newSrcType;
        this.driverClassName = newDriverClassName;
        this.host = newHost;
        this.port = newPort;
        this.db = newDb;
        this.username = newUsername;
        this.password = newPwd;
        this.rmark = newRmark;
        this.status = newStatus;
    }
}
