package com.iscas.apiservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author wbq
 * @version 1.0
 * @title PluginAclDetail
 * @description
 * @create 2023/11/9 15:06
 */

@Data
@TableName(value = "plugin_acl_detail")
public class PluginAclDetail {
    @Id
    @JsonProperty(value = "aclDetailId")
    @TableId(value = "acl_detail_id", type = IdType.AUTO)
    private int aclDetailId;
    private int aclUserId;
    private int strategy;
    private int aclId;

    private String username;
}
