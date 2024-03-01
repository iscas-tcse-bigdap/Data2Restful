package com.iscas.apiservice.pojo;

import lombok.Data;

/**
 * @author wbq
 * @version 1.0
 * @title AclInfo
 * @description
 * @create 2023/11/8 10:03
 */

@Data
public class AclInfo {
    private int aclId;
    private int groupId;
    private int aclUserId;
}
