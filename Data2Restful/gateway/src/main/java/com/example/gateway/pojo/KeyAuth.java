package com.example.gateway.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

/*
 *@title key_auth
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/24 17:12
 */
@Data
@TableName(value = "plugin_key_auth")
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class KeyAuth {
    @Id
    @JsonProperty(value = "keyAuthId")
    @TableId(value = "key_auth_id", type = IdType.AUTO)
    private int keyAuthId;

    private int groupId;
    private String keyName;
    private String keyPwd;
    private int status;

    public KeyAuth(int newKeyAuthId, int newGroupId, String newKeyName, String newKeyPwd, int newStatus) {
        this.keyAuthId = newKeyAuthId;
        this.groupId = newGroupId;
        this.keyName = newKeyName;
        this.keyPwd = newKeyPwd;
        this.status = newStatus;
    }

}
