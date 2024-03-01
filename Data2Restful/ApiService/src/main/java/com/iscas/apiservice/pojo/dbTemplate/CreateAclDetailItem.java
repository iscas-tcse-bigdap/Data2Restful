package com.iscas.apiservice.pojo.dbTemplate;

import lombok.Data;

/**
 * @author wbq
 * @version 1.0
 * @title createAclDetaiiItem
 * @description
 * @create 2023/11/14 14:54
 */

@Data
public class CreateAclDetailItem {
    private int aclUserId;
    private int aclId;

    public CreateAclDetailItem(int newAclUserId, int newAclId) {
        this.aclUserId = newAclUserId;
        this.aclId = newAclId;
    }
}
