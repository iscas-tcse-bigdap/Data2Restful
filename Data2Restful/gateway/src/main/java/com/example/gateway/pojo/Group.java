package com.example.gateway.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;

/*
 *@title Group
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/24 16:34
 */
@Data
@TableName(value = "apigroup")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Group {
    @Id
    @JsonProperty(value = "groupId")
    @TableId(value = "group_id", type = IdType.AUTO)
    private int groupId;

    private String name;
    private String groupDesc;

    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private int keyAuthStatus;
    private int aclStatus;
}
