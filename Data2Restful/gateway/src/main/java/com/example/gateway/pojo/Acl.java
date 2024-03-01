package com.example.gateway.pojo;

import lombok.Data;

/**
 * @author wbq
 * @version 1.0
 * @title Acl
 * @description
 * @create 2023/11/8 10:27
 */

@Data
public class Acl {
    private int aclId;
    private int groupId;
    private int aclUserId;

    public Acl(int newAclId, int newGroupId, int aclName) {
        this.aclId = newAclId;
        this.groupId = newGroupId;
        this.aclUserId = aclName;
    }
}
