package com.iscas.apiservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author wbq
 * @version 1.0
 * @title PluginKeyAuth
 * @description
 * @create 2023/10/25 14:09
 */
@Data
@TableName(value = "plugin_key_auth")
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public final class PluginKeyAuth {
    @Id
    @JsonProperty(value = "keyAuthId")
    @TableId(value = "key_auth_id", type = IdType.AUTO)
    @Schema(description = "key-auth插件ID")
    private int keyAuthId;

    @Schema(example = "1", description = "数据服务组ID")
    private int groupId;

    @Schema(example = "example-key-auth-name", description = "key-auth插件关键字")
    private String keyName;

    @Schema(example = "example-key-auth-pwd", description = "key-auth插件凭据")
    private String keyPwd;

    @Schema(example = "0", description = "插件状态，初始一定为0")
    private int status;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public void setDefaultValue(String newKeyName, String newKeyPwd, int newStatus) {
        this.keyName = newKeyName;
        this.keyPwd = newKeyPwd;
        this.status = newStatus;
        this.updateTime = new Date();
    }

    public PluginKeyAuth(int newKeyAuthId) {
        this.keyAuthId = newKeyAuthId;
    }

    public PluginKeyAuth(int newKeyAuthId, String newKeyName, String pwd) {
        this.keyAuthId = newKeyAuthId;
        this.keyName = newKeyName;
        this.keyPwd = pwd;
    }
}
